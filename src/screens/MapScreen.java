package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import commons.Clock;
import commons.ImageResource;
import commons.Magnitudes;
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
		int repeatX = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_WIDTH + 4) * Master.mapZoom);
		int repeatY = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_HEIGHT + 4) * Master.mapZoom);
		
		Submarine sub = master.getSub();
		
		//Offset for movement of background relative to sub
		double offsetX = (sub.getPosition().getX() * Magnitudes.FEET_PER_PIXEL
				% ImageResource.BG_TILE_WIDTH * -1 ) / Master.mapZoom;
		double offsetY = (sub.getPosition().getY() * Magnitudes.FEET_PER_PIXEL
				% ImageResource.BG_TILE_HEIGHT) / Master.mapZoom;
		
		for (int i = -repeatX; i < repeatX; i++) {
			for (int j = -repeatY; j < repeatY; j++) {
				AffineTransform at = new AffineTransform();
				at.translate(
						(double) (ImageResource.BG_TILE_WIDTH * i) / Master.mapZoom + offsetX + gamePanel.getWidth() / 2,
						(double) (ImageResource.BG_TILE_HEIGHT * j) / Master.mapZoom + offsetY + gamePanel.getHeight() / 2
						);
				at.scale(1 / Master.mapZoom, 1 / Master.mapZoom);
				
				g2d.drawImage(bg[index],
						at,
						null);
			}
		}
		
		//TODO this is for debugging
		g2d.setColor(Color.RED);
		g2d.fillRect(300 + (int)offsetX, 300 + (int)offsetY, 10, 10);
	}

	public void drawSub(Graphics2D g2d) {
		Submarine sub = master.getSub();
		BufferedImage subImage = ImageResource.getSubmarine();
		AffineTransform at = new AffineTransform();
		at.translate(
				gamePanel.getWidth() / 2 - subImage.getWidth() / 2 / Master.mapZoom,
				gamePanel.getHeight() / 2 - subImage.getHeight() / 2 / Master.mapZoom);
		at.rotate(sub.getHeading(), subImage.getWidth() / 2, subImage.getHeight() / 2);
		at.scale(1d / Master.mapZoom, 1d / Master.mapZoom);
		g2d.drawImage(subImage, at, null);
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
