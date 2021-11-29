package firstPackage;

import firstPackage.Objet;
import firstPackage.SpaceInvaders;

public class ObjetAlien extends Objet {
	
	// Vitesse deplacement horizontale des aliens
	private double vitesseDeplacement = 75;
	// La partie ou les aliens existent
	private SpaceInvaders spaceinvaders;
	
	// Constructeur
	public ObjetAlien(SpaceInvaders spaceinvaders, String ref, int x, int y) {
		super(ref, x, y);
		this.spaceinvaders = spaceinvaders;
		dx = -vitesseDeplacement;
	}
	
	// Les aliens se deplacent en fonction d'un temps delta
	public void deplacement(long delta) {
		
		// Si contre le bord gauche et deplacement a gauche, on met a jour la logique
		if ((dx < 0) && (x < 10)) {
			spaceinvaders.updateLogic();
		}
		
		// idem pour bord droit
		if ((dx > 0) && (x > 750)) {
			spaceinvaders.updateLogic();
		}
		
		// Mouvement normal
		super.deplacement(delta);
		
	}
	
	
	
	
	// Mise a jour de la logique des aliens
	public void doLogic() {
		
		// Change de sens et descend d'un etage
		dx = -dx;
		y += 10;
		
		// Si en bas de l'ecran, partie perdue
		if (y > 570) {
			spaceinvaders.notifyDeath();
		}
		
	}
	
	// Notification de collision
	
	public void collisionAvec(Objet autre) {
		
	}

}
