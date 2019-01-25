package screens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import commons.ImageResource;
import commons.Screen;
import main.GamePanel;
import master.Master;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class MapScreen implements Screen {
	
	private Master master;
	private GamePanel gamePanel;
	
	/**
	 * 
	 */
	public MapScreen(Master master, GamePanel gamePanel) {
		this.master = master;
		this.gamePanel = gamePanel;
	}

	@Override
	public void initializeScreen() {
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		drawSub(g2d);
	}

	public void drawSub(Graphics2D g2d) {
		Submarine sub = master.getSub();
		BufferedImage subImage = ImageResource.getSubmarine();
		AffineTransform at = AffineTransform.getTranslateInstance(sub.getPosition().getX(), -sub.getPosition().getY());
		at.rotate(sub.getHeading(), subImage.getWidth() / 2, subImage.getHeight() / 2);
		g2d.drawImage(subImage, at, null);
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
