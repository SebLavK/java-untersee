package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import commons.ImageResource;
import commons.Magnitudes;
import commons.Screen;
import commons.Vessel;
import main.GamePanel;
import master.Camera;
import master.Master;

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
		drawShips(g2d);
		g2d.dispose();
		g.dispose();
	}
	
	public void drawBackground(Graphics2D g2d) {
		double zoom = master.getScenario().getCamera().getZoom();
		int index = master.getTickCount() / 3 % bg.length;
		//Fill the edges
		int repeatX = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_WIDTH + 4) * zoom);
		int repeatY = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_HEIGHT + 4) * zoom);
		
		Camera camera = master.getScenario().getCamera();
		
		//Offset for movement of background relative to sub
		double offsetX = (camera.getPosition().getX() * Magnitudes.FEET_PER_PIXEL
				% ImageResource.BG_TILE_WIDTH * -1 ) / zoom;
		double offsetY = (camera.getPosition().getY() * Magnitudes.FEET_PER_PIXEL
				% ImageResource.BG_TILE_HEIGHT) / zoom;
		
		for (int i = -repeatX; i < repeatX; i++) {
			for (int j = -repeatY; j < repeatY; j++) {
				AffineTransform at = new AffineTransform();
				at.translate(
						(double) (ImageResource.BG_TILE_WIDTH * i) / zoom + offsetX + gamePanel.getWidth() / 2,
						(double) (ImageResource.BG_TILE_HEIGHT * j) / zoom + offsetY + gamePanel.getHeight() / 2
						);
				at.scale(1 / zoom, 1 / zoom);
				
				g2d.drawImage(bg[index],
						at,
						null);
			}
		}
		
		//TODO this is for debugging
		g2d.setColor(Color.RED);
		g2d.fillRect(300 + (int)offsetX, 300 + (int)offsetY, 10, 10);
	}
	
	public void drawVessel(Graphics2D g2d, Vessel vessel) {
		Camera camera = master.getScenario().getCamera();
		BufferedImage vesselImage = ImageResource.getImageForVessel(vessel);
		AffineTransform at = new AffineTransform();
		double zoom = master.getScenario().getCamera().getZoom();
		
		Point2D relPos = camera.relativePositionOf(vessel);
		at.translate(
				(relPos.getX()  * Magnitudes.FEET_PER_PIXEL - vesselImage.getWidth() / 2) / zoom + gamePanel.getWidth() / 2,
				( - relPos.getY()  * Magnitudes.FEET_PER_PIXEL - vesselImage.getHeight() / 2) / zoom + gamePanel.getHeight() / 2
				);
		at.rotate(vessel.getHeading(), vesselImage.getWidth() / 2 / zoom, vesselImage.getHeight() / 2 / zoom);
		at.scale(1 / zoom, 1 / zoom);
		
		g2d.drawImage(vesselImage, at, null);
	}

	public void drawSub(Graphics2D g2d) {
		drawVessel(g2d, master.getScenario().getSub());
//		Submarine sub = master.getScenario().getSub();
//		BufferedImage subImage = ImageResource.getSubmarine();
//		AffineTransform at = new AffineTransform();
//		at.translate(
//				gamePanel.getWidth() / 2 - subImage.getWidth() / 2 / Master.mapZoom,
//				gamePanel.getHeight() / 2 - subImage.getHeight() / 2 / Master.mapZoom);
//		at.rotate(sub.getHeading(), subImage.getWidth() / 2, subImage.getHeight() / 2);
//		at.scale(1d / Master.mapZoom, 1d / Master.mapZoom);
//		g2d.drawImage(subImage, at, null);
	}
	
	public void drawShips(Graphics2D g2d) {
		for (Vessel vessel : master.getScenario().getShips()) {
			drawVessel(g2d, vessel);
		}
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
