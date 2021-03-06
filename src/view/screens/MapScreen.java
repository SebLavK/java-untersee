package view.screens;

import commons.Clock;
import commons.Magnitudes;
import controller.master.Camera;
import controller.master.Master;
import model.ships.Vessel;
import model.submarine.Submarine;
import view.ImageResource;
import view.Screen;
import view.panels.GamePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

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
	private BufferedImage sunglare;
	private BufferedImage crtShadow;
	
	private boolean fadeIn;
	
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
		sunglare = ImageResource.getSunglare();
		crtShadow = ImageResource.getCrtShadow();
		
		renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		targetFont = ImageResource.getMainFont();
		targetFont = targetFont.deriveFont(Font.PLAIN, TARGET_FONT_SIZE);
		
		fadeIn = true;
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(renderHints);
		drawBackground(g2d);
		
		drawSub(g2d);
		master.getScenario().getSub().getSonar().getContacts().forEach(e -> drawVessel(g2d, e));
		master.getScenario().getProjectiles().forEach(e -> drawVessel(g2d, e));
		
		drawForeground(g2d);
		if (fadeIn) {
			fadeIn(g2d);
		}
		g2d.dispose();
		g.dispose();
	}
	
	public void fadeIn(Graphics2D g2d) {
		if (Clock.getTickCount() < Clock.FPS * 2) {
			g2d.setColor(new Color(0, 0, 0,
					(int) Math.round(((double)Clock.FPS * 2 - (double)Clock.getTickCount()) / ((double)Clock.FPS * 3) * 255d)));
			g2d.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		} else {
			
		}
	}
	
	public void drawForeground(Graphics2D g2d) {
		Camera camera = master.getScenario().getCamera(); 
		double zoom = camera.getZoom();
		if (zoom > ZOOM_DESIGNATION_CUTOFF) {
			if (zoom < Camera.STRATEGY_ZOOM) {
//			g2d.setColor(getTransitionColor(zoom, color));
				Composite comp = g2d.getComposite();
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
						(float) ((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF))));
				g2d.drawImage(crtShadow, 0, 0, null);
				g2d.setComposite(comp);
			} else {
				g2d.drawImage(crtShadow, 0, 0, null);
			}
		}
	}
	
	public void drawBackground(Graphics2D g2d) {
		double zoom = master.getScenario().getCamera().getZoom();
		//Offset for movement of background relative to sub
		Camera camera = master.getScenario().getCamera();
		double offsetX;
		double offsetY;
		
		if (zoom < Camera.STRATEGY_ZOOM) {
			//change of background animation frame every 5 frames
			int index = Clock.getTickCount() / 5 % bg.length;
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
			AffineTransform sat = new AffineTransform();
			sat.translate(gamePanel.getWidth() / 2 - sunglare.getWidth() / 2, gamePanel.getHeight() / 2 - sunglare.getHeight() / 2);
			sat.rotate(Math.PI/3, sunglare.getWidth() / 2, sunglare.getHeight() / 2);
			g2d.drawImage(sunglare, sat, null);
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
		//How many pixels for a kiloyard mile
		//Should make the grid size 1000 yards
		int gridSize = (int) Math.round(1 / zoom * Magnitudes.FEET_PER_PIXEL * Magnitudes.FEET_PER_KYD);
		int horLines = gamePanel.getHeight() / gridSize + 2;
		int verLines = gamePanel.getWidth() / gridSize + 2;
		horLines /= 2;
		verLines /= 2;
		double offsetX =(camera.getPosition().getX() * Magnitudes.FEET_PER_PIXEL / zoom % gridSize * -1);
		double offsetY =(camera.getPosition().getY() * Magnitudes.FEET_PER_PIXEL / zoom % gridSize);
		int posX;
		int posY;
		g2d.setColor(GRID_COLOR);
		for (int i = -verLines; i < horLines; i++) {
			posY = (int) Math.round(gridSize * i + offsetY + gamePanel.getHeight() / 2);
			g2d.drawLine(0, posY, gamePanel.getWidth(), posY);
		}
		for (int i = -horLines; i < verLines; i++) {
			posX = (int) Math.round(gridSize * i + offsetX + gamePanel.getWidth() / 2);
			g2d.drawLine(posX, 0, posX, gamePanel.getHeight());
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
		if (zoom < Camera.STRATEGY_ZOOM) {
			AffineTransform at = new AffineTransform();
			at.translate(screenX, screenY);
			at.rotate(vessel.getHeading(), vesselImage.getWidth() / 2 / zoom, vesselImage.getHeight() / 2 / zoom);
			at.scale(1 / zoom, 1 / zoom);
			
			g2d.drawImage(vesselImage, at, null);
		}
	}
	
	/**
	 * @param g2d
	 * @param vessel
	 * @param zoom
	 */
	private void drawVesselDesignation(Graphics2D g2d, Vessel vessel, double screenX, double screenY, double zoom, Color color) {
		//If the zoom is far away enough
		if (zoom >= ZOOM_DESIGNATION_CUTOFF / 4) {
			if (zoom < Camera.STRATEGY_ZOOM / 4) {
//				color = getTransitionColor(zoom, color);
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
						(int) Math.round((zoom - ZOOM_DESIGNATION_CUTOFF / 4) / (Camera.STRATEGY_ZOOM / 4 - ZOOM_DESIGNATION_CUTOFF / 4) * 255));
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

//	/**
//	 * @param zoom
//	 * @param color
//	 * @return
//	 */
//	private Color getTransitionColor(double zoom, Color color) {
//		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Math.round((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF) * 255));
//System.out.println((int) Math.round((zoom - ZOOM_DESIGNATION_CUTOFF) / (Camera.STRATEGY_ZOOM - ZOOM_DESIGNATION_CUTOFF) * 255));
//		return color;
//	}

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
	
	@Override
	public void tick() {
		
	}

	/* (non-Javadoc)
	 * @see view.Screen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
