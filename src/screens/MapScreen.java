package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class MapScreen implements Screen {
	
	public static final double ZOOM_DESIGNATION_CUTOFF = 2;
	public static final int TARGET_FONT_SIZE = 10;
	public static final Color TARGET_NEUTRAL_COLOR = new Color(63, 186, 81);
	public static final Color TARGET_FRIENDLY_COLOR = new Color(63, 104, 186);
	public static final Color TARGET_ENEMY_COLOR = new Color(186, 63, 63);
	public static final double TARGET_OFFSET_X = 0;
	public static final double TARGET_OFFSET_Y = 30;
	
	private Master master;
	private GamePanel gamePanel;
	private Font targetFont;
	
	private BufferedImage[] bg;
	
	RenderingHints renderHints;
	
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
		
		renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		targetFont = ImageResource.getMainFont();
		targetFont = targetFont.deriveFont(Font.PLAIN, TARGET_FONT_SIZE);
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(renderHints);
		drawBackground(g2d);
		drawSub(g2d);
		drawShips(g2d);
		g2d.dispose();
		g.dispose();
	}
	
	public void drawBackground(Graphics2D g2d) {
		double zoom = master.getScenario().getCamera().getZoom();
		//change of background animation frame every 5 frames
		int index = master.getTickCount() / 5 % bg.length;
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
		BufferedImage vesselImage = ImageResource.getImageForVessel(vessel);
		Camera camera = master.getScenario().getCamera();
		Point2D relPos = camera.relativePositionOf(vessel);
		double zoom = master.getScenario().getCamera().getZoom();
		double screenX = transformCoordX(vesselImage, relPos, zoom);
		double screenY = transformCoordY(vesselImage, relPos, zoom);
		
		drawTrueVessel(g2d, vessel, vesselImage, zoom, screenX, screenY);
		drawVesselDesignation(g2d, vessel, screenX, screenY, zoom, TARGET_ENEMY_COLOR);
	}

	/**
	 * @param vesselImage
	 * @param relPos
	 * @param zoom
	 * @return
	 */
	private double transformCoordX(BufferedImage vesselImage, Point2D relPos, double zoom) {
		double screenX = (relPos.getX()  * Magnitudes.FEET_PER_PIXEL - vesselImage.getWidth() / 2) / zoom + gamePanel.getWidth() / 2;
		return screenX;
	}

	/**
	 * @param vesselImage
	 * @param relPos
	 * @param zoom
	 * @return
	 */
	private double transformCoordY(BufferedImage vesselImage, Point2D relPos, double zoom) {
		double screenY = ( - relPos.getY()  * Magnitudes.FEET_PER_PIXEL - vesselImage.getHeight() / 2) / zoom + gamePanel.getHeight() / 2;
		return screenY;
	}


	/**
	 * @param g2d
	 * @param vessel
	 * @param vesselImage
	 * @param zoom
	 * @param screenY
	 * @param screenX
	 */
	private void drawTrueVessel(Graphics2D g2d, Vessel vessel, BufferedImage vesselImage, double zoom, double screenX,
			double screenY) {
		AffineTransform at = new AffineTransform();
		at.translate(screenX, screenY);
		at.rotate(vessel.getHeading(), vesselImage.getWidth() / 2 / zoom, vesselImage.getHeight() / 2 / zoom);
		at.scale(1 / zoom, 1 / zoom);
		
		g2d.drawImage(vesselImage, at, null);
	}
	
	/**
	 * @param g2d
	 * @param vessel
	 * @param zoom
	 */
	private void drawVesselDesignation(Graphics2D g2d, Vessel vessel, double screenX, double screenY, double zoom, Color color) {
		//If the zoom is far away enough
		if (zoom >= ZOOM_DESIGNATION_CUTOFF) {
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF) * 255));
			g2d.setColor(color);
			g2d.setFont(targetFont);
			g2d.drawRect((int) screenX, (int) screenY, 20, 20);
			g2d.drawString(vessel.getDesignation(),
					(float)(screenX + TARGET_OFFSET_X),
					(float)(screenY + TARGET_OFFSET_Y)
					);
		}
	}

	public void drawSub(Graphics2D g2d) {
		Submarine sub = master.getScenario().getSub();
		double subDepth = sub.getDepth();
		int subIndex = subIndexFromDepth(subDepth);
		Camera camera = master.getScenario().getCamera();
		BufferedImage vesselImage = ImageResource.getSubmarine()[subIndex];
		double zoom = master.getScenario().getCamera().getZoom();
		Point2D relPos = camera.relativePositionOf(sub);
		double screenX = transformCoordX(vesselImage, relPos, zoom);
		double screenY = transformCoordY(vesselImage, relPos, zoom);
		drawTrueVessel(g2d, sub, vesselImage, zoom, screenX, screenY);
		drawVesselDesignation(g2d, sub, screenX, screenY, zoom, TARGET_FRIENDLY_COLOR);
	}

	/**
	 * @param subDepth
	 * @return
	 */
	private int subIndexFromDepth(double subDepth) {
		int subIndex;
		//Different shaded sprites for different depths
		if (subDepth <= Submarine.SURFACE_DEPTH) {
			subIndex = 0;
		} else if (subDepth <= Submarine.SAIL_DEPTH){
			subIndex = 1;
		} else if (subDepth <= Submarine.PERISCOPE_DEPTH){
			subIndex = 2;
		} else if (subDepth <= Submarine.TEST_DEPTH * 2 / 10){
			subIndex = 3;
		} else if (subDepth <= Submarine.TEST_DEPTH * 3 / 10){
			subIndex = 4;
		} else {
			subIndex = 5;
		}
		return subIndex;
	}
	
	public void drawShips(Graphics2D g2d) {
		for (Vessel vessel : master.getScenario().getShips()) {
			drawVessel(g2d, vessel);
		}
	}
	
	@Override
	public void tick() {
		
	}

}
