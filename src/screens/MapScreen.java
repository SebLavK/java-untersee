package screens;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
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
	
	public static final double ZOOM_DESIGNATION_CUTOFF = 4;
	public static final int TARGET_FONT_SIZE = 10;
	public static final Color TARGET_NEUTRAL_COLOR = new Color(63, 186, 81);
	public static final Color TARGET_FRIENDLY_COLOR = new Color(63, 104, 186);
	public static final Color TARGET_ENEMY_COLOR = new Color(186, 63, 63);
	public static final double TARGET_OFFSET_X = 12;
	public static final double TARGET_OFFSET_Y = -2;
	
	public static final Color STRATEGY_COLOR = new Color(0x0d,0x3f,0x58);
	public static final Color GRID_COLOR = new Color(0x19,0x68,0x53);
	
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
		//Offset for movement of background relative to sub
		Camera camera = master.getScenario().getCamera();
		double offsetX;
		double offsetY;
		
		if (zoom < Camera.STRATEGY_ZOOM) {
			//change of background animation frame every 5 frames
			int index = master.getTickCount() / 5 % bg.length;
			//Fill the edges
			int repeatX = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_WIDTH + 4) * zoom);
			int repeatY = (int) ((gamePanel.getWidth() / ImageResource.BG_TILE_HEIGHT + 4) * zoom);
			offsetX = (camera.getPosition().getX() * Magnitudes.FEET_PER_PIXEL % ImageResource.BG_TILE_WIDTH * -1)
					/ zoom;
			offsetY = (camera.getPosition().getY() * Magnitudes.FEET_PER_PIXEL % ImageResource.BG_TILE_HEIGHT) / zoom;
			for (int i = -repeatX; i < repeatX; i++) {
				for (int j = -repeatY; j < repeatY; j++) {
					AffineTransform at = new AffineTransform();
					at.translate((double) (ImageResource.BG_TILE_WIDTH * i) / zoom + offsetX + gamePanel.getWidth() / 2,
							(double) (ImageResource.BG_TILE_HEIGHT * j) / zoom + offsetY + gamePanel.getHeight() / 2);
					at.scale(1 / zoom, 1 / zoom);

					g2d.drawImage(bg[index], at, null);
				}
			} 
			//TODO this is for debugging
			g2d.setColor(Color.RED);
			g2d.fillRect(300 + (int)offsetX, 300 + (int)offsetY, 10, 10);
		}
		
		if (zoom > ZOOM_DESIGNATION_CUTOFF) {
			//Draw the "strategy view" background
			Color color = STRATEGY_COLOR;
			g2d.setColor(color);
			if (zoom < Camera.STRATEGY_ZOOM) {
//				g2d.setColor(getTransitionColor(zoom, color));
				Composite comp = g2d.getComposite();
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF))));
				g2d.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
				drawGrid(g2d, zoom, camera);
				g2d.setComposite(comp);
			} else {
				g2d.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
				drawGrid(g2d, zoom, camera);
			}
		}
		
	}
	
	public void drawGrid(Graphics2D g2d, double zoom, Camera camera) {
		//How many pixels for a nautical mile
		int gridSize = (int) Math.round(1 / zoom * Magnitudes.FEET_PER_PIXEL * Magnitudes.FEET_PER_NM / 5);
		int horLines = gamePanel.getHeight() / gridSize + 1;
		int verLines = gamePanel.getWidth() / gridSize + 1;
		
		g2d.setColor(GRID_COLOR);
		for (int i = 0; i < horLines; i++) {
			g2d.drawLine(0, i * gridSize, gamePanel.getWidth(), i * gridSize);
		}
		for (int i = 0; i < verLines; i++) {
			g2d.drawLine(i * gridSize, 0, i * gridSize, gamePanel.getHeight());
		}
	}
	
	public void drawVessel(Graphics2D g2d, Vessel vessel) {
		BufferedImage vesselImage = ImageResource.getImageForVessel(vessel);
		Camera camera = master.getScenario().getCamera();
		Point2D relPos = camera.relativePositionOf(vessel);
		double zoom = master.getScenario().getCamera().getZoom();
		double screenX = transformCoordXForSprite(vesselImage, relPos, zoom);
		double screenY = transformCoordYForSprite(vesselImage, relPos, zoom);
		
		drawTrueVessel(g2d, vessel, vesselImage, zoom, screenX, screenY);
		screenX = transformCoordX(relPos, zoom);
		screenY = transformCoordY(relPos, zoom);
		drawVesselDesignation(g2d, vessel, screenX, screenY, zoom, TARGET_ENEMY_COLOR);
	}
	
	private double transformCoordX(Point2D relPos, double zoom) {
		return (relPos.getX() * Magnitudes.FEET_PER_PIXEL) / zoom + gamePanel.getWidth() / 2;
	}
	
	private double transformCoordY(Point2D relPos, double zoom) {
		return ( - relPos.getY() * Magnitudes.FEET_PER_PIXEL) / zoom + gamePanel.getHeight() / 2;
	}

	/**
	 * @param vesselImage
	 * @param relPos
	 * @param zoom
	 * @return
	 */
	private double transformCoordXForSprite(BufferedImage vesselImage, Point2D relPos, double zoom) {
		double screenX = (relPos.getX()  * Magnitudes.FEET_PER_PIXEL - vesselImage.getWidth() / 2) / zoom + gamePanel.getWidth() / 2;
		return screenX;
	}

	/**
	 * @param vesselImage
	 * @param relPos
	 * @param zoom
	 * @return
	 */
	private double transformCoordYForSprite(BufferedImage vesselImage, Point2D relPos, double zoom) {
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
			if (zoom < Camera.STRATEGY_ZOOM) {
				color = getTransitionColor(zoom, color);
			}
			g2d.setColor(color);
			g2d.setFont(targetFont);
			g2d.drawRect((int) screenX - 10, (int) screenY - 10, 20, 20);
			g2d.drawString(vessel.getDesignation(),
					(float)(screenX + TARGET_OFFSET_X),
					(float)(screenY + TARGET_OFFSET_Y)
					);
		}
	}

	/**
	 * @param zoom
	 * @param color
	 * @return
	 */
	private Color getTransitionColor(double zoom, Color color) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF) * 255));
		return color;
	}

	public void drawSub(Graphics2D g2d) {
		Submarine sub = master.getScenario().getSub();
		double subDepth = sub.getDepth();
		int subIndex = subIndexFromDepth(subDepth);
		Camera camera = master.getScenario().getCamera();
		BufferedImage vesselImage = ImageResource.getSubmarine()[subIndex];
		double zoom = master.getScenario().getCamera().getZoom();
		Point2D relPos = camera.relativePositionOf(sub);
		double screenX = transformCoordXForSprite(vesselImage, relPos, zoom);
		double screenY = transformCoordYForSprite(vesselImage, relPos, zoom);
		drawTrueVessel(g2d, sub, vesselImage, zoom, screenX, screenY);
		screenX = transformCoordX(relPos, zoom);
		screenY = transformCoordY(relPos, zoom);
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
