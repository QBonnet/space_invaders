package firstPackage;

import firstPackage.Objet;
import firstPackage.ObjetAlien;
import firstPackage.SpaceInvaders;

public class ObjetTirEnnemi extends Objet {
	
	// Vitesse verticale des tirs du joueur
	private double vitesseDeplacement = 200;
	// Partie ou se trouve les tirs
	private SpaceInvaders spaceinvaders;
	// Vrai si le tir a touchee
	private boolean touche = false;
	
	
	// Constructeur
	public ObjetTirEnnemi(SpaceInvaders spaceinvaders, String sprite, int x, int y) {
		super(sprite, x, y);
		this.spaceinvaders = spaceinvaders;
		dy = vitesseDeplacement;
	}
	
	// Deplacement du tir en fonction d'un temps delta
	public void deplacement(long delta) {
		
		// deplacement normal
		super.deplacement(delta);
		
		// Si au bout de l'ecran, le tir disparait
		if (y > 1000) {
			spaceinvaders.supprimerObjet(this);
			
		}
	}
	
	// Notification de collision
	
	public void collisionAvec(Objet autre) {
		
		
		
	}

}
