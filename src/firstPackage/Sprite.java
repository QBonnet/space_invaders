package firstPackage;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
	
	// L'image a dessiner pour ce sprite
	private Image image;
	
	// Constructeur : réer le sprite a partir d'une image
	public Sprite(Image image) {
		this.image = image;
	}
	
	// Largeur du sprite ( pixels )
	public int getWidth() {
		return image.getWidth(null);
	}
	
	// Hauteur du sprite ( pixels )
	public int getHeight() {
		return image.getHeight(null);
	}
	
	// Dessine le sprite à l'endroit prévu
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);
	}
}
