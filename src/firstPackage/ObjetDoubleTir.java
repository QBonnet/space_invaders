package firstPackage;

import firstPackage.Objet;
import firstPackage.ObjetAlien;
import firstPackage.SpaceInvaders;

public class ObjetDoubleTir extends Objet {
	
	// Vitesse verticale des tirs du joueur
	private double vitesseDeplacement = -300;
	// Partie ou se trouve les tirs
	private SpaceInvaders spaceinvaders;
	// Vrai si le tir a touchee
	private boolean touche = false;
	
	
	// Constructeur
	public ObjetDoubleTir(SpaceInvaders spaceinvaders, String sprite, int x, int y) {
		super(sprite, x, y);
		this.spaceinvaders = spaceinvaders;
		dy = vitesseDeplacement;
	}
	
	// Deplacement du tir en fonction d'un temps delta
	public void deplacement(long delta) {
		
		// deplacement normal
		super.deplacement(delta);
		
		// Si au bout de l'ecran, le tir disparait
		if (y < -100) {
			spaceinvaders.supprimerObjet(this);
			
		}
	}
	
	// Notification de collision
	
	public void collisionAvec(Objet autre) {
		
		// Eviter les doubles touche
		if (touche) {
			return;
		}
		
		// Si touche un alien, il est mort
		if (autre instanceof ObjetAlien) {
			// Supprimer les objets
			spaceinvaders.supprimerObjet(this);
			spaceinvaders.supprimerObjet(autre);
			// Preciser au jeu qu'un alien est mort
			spaceinvaders.notifyAlienKilled();
			touche = true;
		}
	}

}