package firstPackage;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Objet {
	
	// Coordonnee x de l'objet
	protected double x;
	// Coordonnee y de l'objet
	protected double y;
	// Le sprite representant l'objet
	protected Sprite sprite;
	// Vitesse horizontale de l'objet ( pixels/sec )
	protected double dx;
	// Vitesse verticale de l'objet ( pixels/sec )
	protected double dy;
	// Rectangle utilise pour gerer les colisions de l'objet
	private Rectangle moi = new Rectangle();
	// Rectangle pour gérer les autres objets pendant une colision
	private Rectangle lui = new Rectangle();
	
	
	// Constructeur : ref = localisation du sprite, x&y = position initial de l'objet
	public Objet(String ref, int x, int y) {
		this.sprite = ListeSprite.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	
	// Demande que l'objet se deplace seul au bout d'un moment delta
	public void deplacement(long delta) {
		
		// Met à jour la position de l'objet en fonction de sa vitesse
		x += (delta*dx) / 1000;
		y += (delta*dy) / 1000;
		
	}
	
	// Gerer la vitesse horizontale de l'objet ( pixels/sec )
	public void setDeplacementHorizontale(double dx) {
		this.dx = dx;
	}
	
	// Gerer la vitesse verticale de l'objet ( pixels/sec )
	public void setDeplacementVerticale(double dy) {
		this.dy = dy;
	}
	
	// Retourne la vitesse horizontale de l'objet ( pixels/sec )
	public double getDeplacementHorizontale() {
		return dx;
	}
	
	// Retourne la vitesse verticale de l'objet ( pixels/sec )
	public double getDeplacementVerticale() {
		return dy;
	}
	
	// Dessine l'objet dans le graphics context dedie
	public void draw(Graphics g) {
		sprite.draw(g, (int) x, (int) y);
	}
	
	// Execute la logique associe à l'objet
	public void doLogic() {
		
	}
	
	// Retourne la coordonnee x de l'objet
	public int getX() {
		return (int) x;
	}
	
	// Retourne la coordonnee y de l'objet
	public int getY() {
		return (int) y;
	}
	
	// Verification si l'objet entre en colision avec un autre, autre = l'autre objet de la collision, true si il y a collision
	public boolean collisionsAvec(Objet autre) {
		moi.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		lui.setBounds((int) autre.x, (int) autre.y, autre.sprite.getWidth(), autre.sprite.getHeight());
		return moi.intersects(lui);
	}
	
	// Notification de collision
	public abstract void collisionAvec(Objet autre);

}
