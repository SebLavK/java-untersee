package commons;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;

/**
*@author Sebas Lavigne
*/

public class ImageResource {
	
	public static BufferedImage submarine;
	
	public static void instantiateImages() {
		try {
			submarine = ImageIO.read(Main.class.getResource("../sprites/ShipSubMarineHull.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the submarine
	 */
	public static BufferedImage getSubmarine() {
		return submarine;
	}

}
