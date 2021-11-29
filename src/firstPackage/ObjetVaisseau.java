package firstPackage;

import firstPackage.Objet;
import firstPackage.ObjetAlien;
import firstPackage.SpaceInvaders;

public class ObjetVaisseau extends Objet {
	
	// La partie dans laquelle le vaisseau existe
	private SpaceInvaders spaceinvaders;
	
	// Constructeur
	public ObjetVaisseau(SpaceInvaders spaceinvaders, String ref, int x, int y) {
		super(ref, x, y);
		this.spaceinvaders = spaceinvaders;
		
	}
	
	// Deplacer vaisseau seul au bout d'un moment delta
	public void deplacement(long delta) {
		
		// Si vers la gauche et on eset contre le bord gauche, ne pas bouger
		if ((dx < 0) && (x < 10)) {
			return;
		}
		
		// Si vers la droite et on est contre le bord droit, ne pas bouger
		if ((dx > 0) && (x > 750)) {
			return;
		}
		
		super.deplacement(delta);
	}
	
	// Notification que le vaisseau est en collision
	public void collisionAvec(Objet autre) {
		//Si alien la partie est perdue
		if (autre instanceof ObjetAlien) {
			spaceinvaders.notifyDeath();		
			}
		if (autre instanceof ObjetTirEnnemi) {
			spaceinvaders.notifyDeath();
		}
	}

}
