package firstPackage;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics2D;

import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;




public class SpaceInvaders extends Canvas {
	
	// Strategy utilisé
	private BufferStrategy strategy;
	// Variable pour savoir si le jeu continue
	private boolean continuerJeu =true;
	// Liste des objets utilisés pendant la partie
	private ArrayList objets = new ArrayList();
	// Liste des objets à enlever pendant la partie
	private ArrayList listeSuppr = new ArrayList();
	// L'objet représentant le joueur
	private Objet vaisseau;
	// Vitesse de déplacement du joueur ( pixels/sec )
	private double vitesseDeplacement = 300;
	// Moment où le dernier tir à eu lieu
	private long dernierTir = 0;
	private long dernierTirEnnemi = 0;
	// Interval de temps entre 2 tirs ( ms )
	private long intervalTir = 500;
	private long intervalTirEnnemi = 500;
	// Compteur des aliens encore vivants
	private int compteurAlien;
	// Score du joueur
	public static int score = 0;
	
	
	// Message quand on attends une action sur un touche
	private String message = "";
	// Vrai si le jeu est en pause
	private boolean attente = false;
	// Vrai si le clic gauche est enclenché
	private boolean gaucheActif = false;
	// Vrai si le clic droit est enclenché
	private boolean droitActif = false;
	// Vrai si le joueur tir
	private boolean tirActif = false;
	// Vrai si la logique du jeu doit etre mise a jour
	private boolean logicRequiredThisLoop = false;
	// Nombre aleatoire pour gérer les tirs ennemis
	private int rand;
	
	
	// Constructeur 
	public SpaceInvaders() {
		
		// Creation de la fenetre contenant le jeu
		JFrame conteneur = new JFrame("Space Invaders");
		
		// Contenu de la fenetre et réolution du jeu
		JPanel panneau = (JPanel) conteneur.getContentPane();
		panneau.setPreferredSize(new Dimension(800, 600));
		panneau.setLayout(null);
		
		// Configurer la taille du canvas et le placer dans le conteneur
		setBounds(0, 0, 800, 600);
		panneau.add(this);
		
		// AWT n'utilise pas le repaint, on le fait nous même en accelerated mode
		setIgnoreRepaint(true);
		
		// On rend la fenetre visible
		conteneur.pack();
		conteneur.setResizable(false);
		conteneur.setVisible(true);
		conteneur.setLocationRelativeTo(null);
		conteneur.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// Ajoute un listener pour repondre quand une touche est pressee ( voir plus bas )
		addKeyListener(new KeyInputHandler());
		
		// Recentre l'attention pour les evenements Key
		requestFocus();
		
		// Création de la buffering strategy pour que l'AWT gere l'acceleration graphiquer
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// Initialise les objets du jeu
		initObjets();
		
	}
	
	
	// Creer une nouvelle partie 
	private void demmarrerJeu() {
		
		// Efface les anciens objets et en initialise de nouveaux
		objets.clear();
		initObjets();
		
		// Reinitialise les parametres claviers
		gaucheActif = false;
		droitActif = false;
		tirActif = false;
		
	}
	
	
	// Initialise les objets de début de jeu ( vaisseau et aliens ), tous les objets seront ajoute à une liste global des objets du jeu
	private void initObjets() {
		
		// créer le vaisseau du joueur et le place au centre de l'ecran
		vaisseau = new ObjetVaisseau(this, "sprites/vaisseau.gif", 370, 550);
		objets.add(vaisseau);
		
		// // Creation des aliens
		compteurAlien = 0;
		for (int row=0; row<5; row++) {
			for (int x=0; x<12; x++) {
				Objet alien = new ObjetAlien(this, "sprites/alien.gif", 100+(x*50), (50)+row*30);
				objets.add(alien);
				compteurAlien++;
			}
		}
	}
	
	// Notification que la logique doit etre mise a jour
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	// Enlever un objet du jeu
	public void supprimerObjet(Objet objet) {
		listeSuppr.add(objet);
	}
	
	
	
	
	// Notification que le joueur est mort
	public void notifyDeath() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 600);
		message = "GAME OVER ! SCORE : " +score+ " PTS";
		attente = true;
		
	}
	
	// Notification que la partie est gagné
	public void notifyWin() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 600);
		score = score + 1000;
		message = "GOOD JOB ! SCORE : " +score+ " PTS";
		attente = true;
	}
	
	// Notification qu'un alien est mort
	public void notifyAlienKilled() {
		
		// Decrementation du nbr d'alien, si 0 le joueur gagne
		compteurAlien--;
		score = score + 10;
		
		if (compteurAlien == 0) {
			notifyWin();
		}
		
		// Si encore des aliens, on les acceleres
		for (int i=0; i<objets.size(); i++) {
			Objet objet = (Objet) objets.get(i);
			
			if (objet instanceof ObjetAlien) {
				// vitesse + 2%
				objet.setDeplacementHorizontale(objet.getDeplacementHorizontale() *1.02);
				
			}
		}
	}
	
	// Temps d'attente entre deux tirs du joueur
	public void essaieTir() {
		
		// Verification de si on a assez attendu ou non
		if (System.currentTimeMillis() - dernierTir < intervalTir) {
			return;
		}
		
		// Si attente assez longue, nouveau tir et on enregistre le temps
		dernierTir = System.currentTimeMillis();
		ObjetTir tir = new ObjetTir(this, "sprites/tir.gif", vaisseau.getX()+10, vaisseau.getY()-30);
		objets.add(tir);
	}
	
	// Gestion double tir
	public void essaieDoubleTir() {
		
		// Verification de si on a assez attendu ou non
		if (System.currentTimeMillis() - dernierTir < intervalTir) {
			return;
		}
		
		// Si attente assez longue, nouveau tir et on enregistre le temps
		dernierTir = System.currentTimeMillis();
		ObjetTir doubleTir = new ObjetTir(this, "sprites/doubleTir.gif", vaisseau.getX()+10, vaisseau.getY()-30);
		objets.add(doubleTir);
	}
	
	// Gestion tir ennemi
	public void tirEnnemi() {
				
		// Verification de si on a assez attendu ou non
		if (System.currentTimeMillis() - dernierTirEnnemi < intervalTirEnnemi) {
			return;
		}
				
		// Si attente assez longue, nouveau tir et on enregistre le temps, le nombre rand permet de tirer depuis n'importe ou sur le jeu
		dernierTirEnnemi = System.currentTimeMillis();
		ObjetTirEnnemi tirEnnemi = new ObjetTirEnnemi(this, "sprites/tirEnnemi.gif",  rand = (int)(Math.random() * 800), 200    );
		objets.add(tirEnnemi);
		
	}
	
	
	// La boocle qui gere le jeu
	public void boucle() {
		long tempsDerniereBoucle = System.currentTimeMillis();
		
		// La boucle tourne pendants toute la partie
		while (continuerJeu) {
			// Calculer le temps depuis la derniere boucle
			long delta = System.currentTimeMillis() - tempsDerniereBoucle;
			tempsDerniereBoucle = System.currentTimeMillis();
			
			// Gerer le fond graphique du jeu
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			// Voir si les objets doivent bouger d'eux memes
			if (!attente) {
				for (int i=0; i<objets.size(); i++) {
					Objet objet = (Objet) objets.get(i);
					objet.deplacement(delta);
					
				}
			}
			
			// Redessiner les objets du jeu
			for (int i=0; i<objets.size(); i++) {
				Objet objet = (Objet) objets.get(i);
				objet.draw(g);
			}
			
			// Verifier la collision de entre les objets
			for (int p=0; p<objets.size(); p++) {
				for (int s=p+1; s<objets.size(); s++) {
					Objet moi = (Objet) objets.get(p);
					Objet lui = (Objet) objets.get(s);
					
					if (moi.collisionsAvec(lui)) {
						moi.collisionAvec(lui);
						lui.collisionAvec(moi);
					}
				}
			}
			
			// Supprimer les objets marques
			objets.removeAll(listeSuppr);
			listeSuppr.clear();
			
			// Gerer les differents logiques
			if (logicRequiredThisLoop) {
				for (int i=0; i<objets.size(); i++) {
					Objet objet = (Objet) objets.get(i);
					objet.doLogic();
				}
				
				logicRequiredThisLoop = false;
			
			}
			
			// Gestion de l'attente 
			if (attente) {
				g.setColor(Color.white);
				g.drawString(message, (800-g.getFontMetrics().stringWidth(message))/2,250);
			
			}
			
			// Reinitiliasation
			g.dispose();
			strategy.show();
			
			// Gerer le deplacement du vaisseau
			vaisseau.setDeplacementHorizontale(0);
			
			if ((gaucheActif) && (!droitActif)) {
				vaisseau.setDeplacementHorizontale(-vitesseDeplacement);
				
			} else if ((droitActif) && (!gaucheActif)) {
				vaisseau.setDeplacementHorizontale(vitesseDeplacement);
			}
			
			// Gestion des tirs doubles ou simple, et gestion des tirs ennemis en fonction des notres
			if (tirActif) {
				// si score trop faible, les tirs sont simples
				if ( score < 100 ) {
					essaieTir();
					tirEnnemi();
				}
				// si score eleve, les tirs deviennent doubles
				if ( score >= 100) {
					essaieDoubleTir();
					tirEnnemi();
				}
				
			}
			
			// Faire une pause
			try { Thread.sleep(10);} catch (Exception e) {}
		}
	}
		
		
		// Gestion des entrees du clavie
	private class KeyInputHandler extends KeyAdapter{
			
		
			
		// Notification touche pressee
		public void keyPressed(KeyEvent e) {
			if (attente) {
				return;
			}
				
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				gaucheActif = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				droitActif = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				tirActif = true;
			}
		}
			
		// Notification touche relachee
		public void keyReleased(KeyEvent e) {
			if (attente) {
				return;
			}
				
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				gaucheActif = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				droitActif = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				tirActif = false;
			}
			
			
		}
		
		
			
		
		
	}
	
	

	
	
	public static void main(String argv[]) {
		
		SpaceInvaders si = new SpaceInvaders();
		
		
		si.boucle();
	}

}
