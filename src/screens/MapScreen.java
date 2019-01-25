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
	
	private BufferedImage[] bg;
	
	/**
	 * 
	 */
	public MapScreen(Master master, GamePanel gamePanel) {
		this.master = master;
		this.gamePanel = gamePanel;
		initializeScreen();
	}

	@Override
	public void initializeScreen() {
		bg = ImageResource.getBackground();
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		drawBackground(g2d);
		drawSub(g2d);
		g2d.dispose();
		g.dispose();
	}
	
	public void drawBackground(Graphics2D g2d) {
		int index = Master.tickCount / 3 % bg.length;
		//Fill the edges
		int repeatX = gamePanel.getWidth() / ImageResource.BG_TILE_WIDTH + 1;
		int repeatY = gamePanel.getWidth() / ImageResource.BG_TILE_HEIGHT + 1;
		
		for (int i = 0; i < repeatX; i++) {
			for (int j = 0; j < repeatY; j++) {
				g2d.drawImage(bg[index], ImageResource.BG_TILE_WIDTH * i, ImageResource.BG_TILE_HEIGHT * j, null);
			}
		}
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
