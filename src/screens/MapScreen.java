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
		int repeatX = (gamePanel.getWidth() / ImageResource.BG_TILE_WIDTH + 2) * Master.mapZoom;
		int repeatY = (gamePanel.getWidth() / ImageResource.BG_TILE_HEIGHT + 2) * Master.mapZoom;
		
		Submarine sub = master.getSub();
		
		//Offset for movement of background relative to sub
		int offsetX = (int) (sub.getPosition().getX() / Master.mapZoom
				% ImageResource.BG_TILE_WIDTH)  * -1;
		int offsetY = (int) (sub.getPosition().getY() / Master.mapZoom
				% ImageResource.BG_TILE_HEIGHT);
		
		
		for (int i = -1; i < repeatX; i++) {
			for (int j = -1; j < repeatY; j++) {
				g2d.drawImage(bg[index],
						ImageResource.BG_TILE_WIDTH * i / Master.mapZoom + offsetX,
						ImageResource.BG_TILE_HEIGHT * j / Master.mapZoom + offsetY,
						(int) (bg[0].getWidth() / Master.mapZoom),
						(int) (bg[0].getHeight() / Master.mapZoom),
						null);
			}
		}
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
