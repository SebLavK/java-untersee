package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import commons.Screen;
import main.GamePanel;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class MapScreen implements Screen {
	
	private BufferedImage subImage;
	private Submarine sub;
	private GamePanel gamePanel;
	
	/**
	 * 
	 */
	public MapScreen(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void initializeScreen() {
		try {
			subImage = ImageIO.read(getClass().getResource("../sprites/ship_destroyed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void drawScreen(Graphics g) {
		drawSprite(g);
	}

	public void drawSprite(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(sub.getPosition().getX(), -sub.getPosition().getY());
		at.rotate(sub.getHeading(), subImage.getWidth() / 2, subImage.getHeight() / 2);
		g2d.drawImage(subImage, at, null);
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the sub
	 */
	public Submarine getSub() {
		return sub;
	}

	/**
	 * @param sub the sub to set
	 */
	public void setSub(Submarine sub) {
		this.sub = sub;
	}
	
	

}
