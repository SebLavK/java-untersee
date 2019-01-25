package commons;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;

/**
*@author Sebas Lavigne
*/

public class ImageResource {
	
	public static final int BG_TILE_WIDTH = 324;
	public static final int BG_TILE_HEIGHT = 144;
	
	
	public static BufferedImage submarine;
	public static BufferedImage[] background;
	
	public static void instantiateImages() {
		try {
			submarine = ImageIO.read(Main.class.getResource("../sprites/ShipSubMarineHull.png"));
			loadBackground();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadBackground() throws IOException {
		background = new BufferedImage[21];
		String url;
		for (int i = 0; i < background.length; i++) {
			url = "../sprites/bg/ocean" + String.format("%02d", i+1) + ".png";
			background[i] = ImageIO.read(Main.class.getResource(url));
		}
	}

	/**
	 * @return the submarine
	 */
	public static BufferedImage getSubmarine() {
		return submarine;
	}

	/**
	 * @return the background
	 */
	public static BufferedImage[] getBackground() {
		return background;
	}

	/**
	 * @param background the background to set
	 */
	public static void setBackground(BufferedImage[] background) {
		ImageResource.background = background;
	}
	
	

}
