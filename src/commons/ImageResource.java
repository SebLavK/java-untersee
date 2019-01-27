package commons;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Main;
import ships.Battleship;
import ships.Cruiser;
import ships.Destroyer;
import ships.PatrolShip;
import ships.RescueShip;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class ImageResource {
	
	public static final int BG_TILE_WIDTH = 324;
	public static final int BG_TILE_HEIGHT = 144;
	
	private static Font mainFont;
	
	private static BufferedImage[] submarine;
	private static BufferedImage battleship;
	private static BufferedImage cruiser;
	private static BufferedImage destroyer;
	private static BufferedImage patrolShip;
	private static BufferedImage rescueShip;
	private static BufferedImage[] background;
	
	public static void instantiateImages() {
		try {
			loadFonts();
			loadBackground();
			loadVessels();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFonts() {
		try{
            mainFont = Font.createFont(Font.TRUETYPE_FONT,
            		Main.class.getResourceAsStream("/fonts/joystix monospace.ttf"));
        }
        catch(Exception e){
        	e.printStackTrace();
        }
//		mainFont = mainFont.deriveFont(Font.PLAIN, 10);
	}

	
	public static void loadBackground() throws IOException {
		background = new BufferedImage[21];
		String url;
		for (int i = 0; i < background.length; i++) {
			url = "/sprites/bg/ocean" + String.format("%02d", i+1) + ".png";
			background[i] = ImageIO.read(Main.class.getResource(url));
		}
	}
	
	/**
	 * @throws IOException
	 */
	private static void loadVessels() throws IOException {
		loadSubmarine();
		battleship = ImageIO.read(Main.class.getResource("/sprites/vessels/ShipBattleshipHull.png"));
		cruiser = ImageIO.read(Main.class.getResource("/sprites/vessels/ShipCruiserHull.png"));
		destroyer = ImageIO.read(Main.class.getResource("/sprites/vessels/ShipDestroyerHull.png"));
		patrolShip = ImageIO.read(Main.class.getResource("/sprites/vessels/ShipPatrolHull.png"));
		rescueShip = ImageIO.read(Main.class.getResource("/sprites/vessels/ShipRescue.png"));
	}
	
	public static void loadSubmarine() throws IOException {
		submarine = new BufferedImage[6];
		for (int i = 0; i < submarine.length; i++) {
			submarine[i] = ImageIO.read(Main.class.getResource("/sprites/vessels/sub"+i+".png"));
		}
		
	}
	
	public static BufferedImage getImageForVessel(Vessel vessel) {
		if (vessel instanceof Battleship) {
			return battleship;
		} else if (vessel instanceof Cruiser) {
			return cruiser;
		} else if (vessel instanceof Destroyer) {
			return destroyer;
		} else if (vessel instanceof PatrolShip) {
			return patrolShip;
		} else if (vessel instanceof RescueShip) {
			return rescueShip;
		} else {
			return null;
		}
	}

	/**
	 * @return the submarine
	 */
	public static BufferedImage[] getSubmarine() {
		return submarine;
	}
	
	

	/**
	 * @return the battleship
	 */
	public static BufferedImage getBattleship() {
		return battleship;
	}


	/**
	 * @return the cruiser
	 */
	public static BufferedImage getCruiser() {
		return cruiser;
	}


	/**
	 * @return the destroyer
	 */
	public static BufferedImage getDestroyer() {
		return destroyer;
	}


	/**
	 * @return the patrolShip
	 */
	public static BufferedImage getPatrolShip() {
		return patrolShip;
	}


	/**
	 * @return the rescueShip
	 */
	public static BufferedImage getRescueShip() {
		return rescueShip;
	}


	/**
	 * @return the background
	 */
	public static BufferedImage[] getBackground() {
		return background;
	}

	/**
	 * @return the mainFont
	 */
	public static Font getMainFont() {
		return mainFont;
	}

	
	

}
