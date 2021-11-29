package firstPackage;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import firstPackage.Sprite;

public class ListeSprite {
	
	private static ListeSprite single = new ListeSprite();
	
	public static ListeSprite get() {
		return single;
	}
	
	private HashMap sprites = new HashMap();
	
	
	// Chercher un sprite dans la liste
	public Sprite getSprite(String ref) {
		
		// Si le sprite dans le cache est déja le bon, on le renvoie
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		}
		
		// Sinon on va chercher l'image
		BufferedImage sourceImage = null;
		
		try {
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				fail("reference introuvable: "+ref);
			}
			
			// On utilise ImageIO pour lire l'image
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Impossible de charger: "+ref);
		}
		
		// Creation d'une image accelere a la bonne taille
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// dessine l'image source dans l'image accelere
		image.getGraphics().drawImage(sourceImage,0,0,null);
		
		// cree le sprite, ajoute au cache, le retourne
		Sprite sprite = new Sprite(image);
		sprites.put(ref,sprite);
		
		return sprite;

	}
	
	
	private void fail(String message) {
		
		System.err.println(message);
		System.exit(0);;
	}

}

