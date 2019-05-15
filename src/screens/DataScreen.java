package screens;

import commons.*;
import main.GamePanel;
import master.Master;
import submarine.Submarine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
*@author Sebas Lavigne
*/

public class DataScreen implements Screen {
	
	public static final int NAV_Y = 20;
	public static final int WEAPONS_Y = 140;
	public static final int TARGET_Y = 340;
	public static final int ROW_HEIGHT = 24;
	public static final int HEAD_X = 50;
	public static final int HEAD_LINE_MARGIN = 20;
	public static final int HEAD_LINE_HEIGHT = 3;
	public static final int ROW_0_X = 25;
	public static final int ROW_1_X = 175;
	public static final int ROW_2_X = 325;
	
	public static final int FONT_BIG_SIZE = 16;
	public static final int FONT_SMALL_SIZE = 12;
	
	public static final Color BG_COLOR = new Color(31, 31, 31);
	public static final Color TEXT_COLOR = Color.WHITE;
	public static final Color HEAD_COLOR = new Color(31, 168, 168);

	private Master master;
	private GamePanel dataPanel;
	
	private BufferedImage crtShadow;
	private Font bigFont;
	private Font smallFont;
	
	private boolean fadeIn;
	
	
	public DataScreen(Master master, GamePanel dataPanel) {
		this.master = master;
		this.dataPanel = dataPanel;
		initializeScreen();
	}

	@Override
	public void initializeScreen() {
		crtShadow = ImageResource.getCrtShadow();
		bigFont = ImageResource.getMainFont();
		smallFont = ImageResource.getMainFont();
		bigFont = bigFont.deriveFont(Font.PLAIN, FONT_BIG_SIZE);
		smallFont = smallFont.deriveFont(Font.PLAIN, FONT_SMALL_SIZE);
		
		fadeIn = true;
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		drawSections(g2d);
		drawNavigation(g2d);
		drawWeapons(g2d);
		drawTarget(g2d);
		drawShadows(g2d);
		
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
			g2d.fillRect(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
		} else {
			fadeIn = true;
		}
	}
	
	/**
	 * @param g2d
	 */
	private void drawShadows(Graphics2D g2d) {
		double scaleX = (double)dataPanel.getWidth() / (double)crtShadow.getWidth();
		double scaleY = (double)dataPanel.getHeight() / (double)crtShadow.getHeight();
		
		AffineTransform at1 = new AffineTransform();
		at1.scale(scaleX,
				scaleY * (WEAPONS_Y - NAV_Y) / dataPanel.getHeight());
		AffineTransform at2 = new AffineTransform();
		at2.translate(0, WEAPONS_Y - NAV_Y);
		at2.scale(scaleX,
				scaleY * (TARGET_Y - WEAPONS_Y) / dataPanel.getHeight());
		AffineTransform at3 = new AffineTransform();
		at3.translate(0, TARGET_Y - NAV_Y);
		at3.scale(scaleX,
				scaleY * (dataPanel.getHeight() - TARGET_Y) / dataPanel.getHeight());
		AffineTransform at4 = new AffineTransform();
		at4.translate(0, dataPanel.getHeight() - NAV_Y);
		at4.scale(scaleX,
				scaleY * (TARGET_Y - WEAPONS_Y) / dataPanel.getHeight());
		
		g2d.drawImage(crtShadow, at1, null);
		g2d.drawImage(crtShadow, at2, null);
		g2d.drawImage(crtShadow, at3, null);
		g2d.drawImage(crtShadow, at4, null);
	}

	public void drawSections(Graphics2D g2d) {
		g2d.setColor(BG_COLOR);
		g2d.fillRect(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
		
		g2d.setColor(HEAD_COLOR);
		g2d.fillRect(HEAD_LINE_MARGIN, NAV_Y - HEAD_LINE_HEIGHT, dataPanel.getWidth() - HEAD_LINE_MARGIN * 2, HEAD_LINE_HEIGHT);
		g2d.fillRect(HEAD_LINE_MARGIN, WEAPONS_Y - HEAD_LINE_HEIGHT, dataPanel.getWidth() - HEAD_LINE_MARGIN * 2, HEAD_LINE_HEIGHT);
		g2d.fillRect(HEAD_LINE_MARGIN, TARGET_Y - HEAD_LINE_HEIGHT, dataPanel.getWidth() - HEAD_LINE_MARGIN * 2, HEAD_LINE_HEIGHT);
		g2d.fillRect(HEAD_LINE_MARGIN, dataPanel.getHeight() - HEAD_LINE_HEIGHT, dataPanel.getWidth() - HEAD_LINE_MARGIN * 2, HEAD_LINE_HEIGHT);
		
		g2d.setColor(BG_COLOR);
		g2d.fillRect(HEAD_X - 5, NAV_Y - 10, 14 * 10, 10);
		g2d.fillRect(HEAD_X - 5, WEAPONS_Y - 10, 14 * 7 + 2, 10);
		g2d.fillRect(HEAD_X - 5, TARGET_Y - 10, 14 * 6 + 5, 10);
		g2d.fillRect(HEAD_X - 5, dataPanel.getHeight() - 10, 14 * 3 + 7, 10);
		
		g2d.setColor(HEAD_COLOR);
		g2d.setFont(bigFont);
		g2d.drawString("Navigation", HEAD_X, NAV_Y);
		g2d.drawString("Weapons", HEAD_X, WEAPONS_Y);
		g2d.drawString("Target", HEAD_X, TARGET_Y);
		g2d.drawString("LOG", HEAD_X, dataPanel.getHeight());
	}
	
	public void drawNavigation(Graphics2D g2d) {
		Submarine sub = master.getScenario().getSub();
		g2d.setColor(TEXT_COLOR);
		g2d.setFont(smallFont);
		
		g2d.drawString("HDG  "+ Magnitudes.radiansToHumanDegrees(sub.getHeading()), ROW_0_X, NAV_Y + ROW_HEIGHT * 1);
		g2d.drawString("SPD   "+ Magnitudes.knotsToHuman(sub.getSpeed()) + " kn", ROW_0_X, NAV_Y + ROW_HEIGHT * 2);
		g2d.drawString("DEP "+ Magnitudes.feetToHuman(sub.getDepth()) + " ft", ROW_0_X, NAV_Y + ROW_HEIGHT * 3);
		
	}
	
	/**
	 * @param g2d
	 */
	private void drawWeapons(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param g2d
	 */
	private void drawTarget(Graphics2D g2d) {
		Submarine sub = master.getScenario().getSub();
		g2d.setColor(TEXT_COLOR);
		g2d.setFont(smallFont);
		if (sub.getTarget() != null) {
			Vessel target = sub.getTarget();
			String range;
			if (sub.distanceTo(target) / Magnitudes.FEET_PER_YARD > 300) {
				range = Magnitudes.feetToKydHuman(sub.distanceTo(target)) + " KYD";
			} else {
				range = Magnitudes.feetToYardHuman(sub.distanceTo(target)) + " YD";
			}
			g2d.drawString("BRG  "+ Magnitudes.radiansToHumanDegrees(sub.bearingTo(target)), ROW_0_X, TARGET_Y + ROW_HEIGHT * 1);
			g2d.drawString("RNG   "+ range, ROW_0_X, TARGET_Y + ROW_HEIGHT * 2);
			g2d.drawString("SOL   "+ String.format("% 3d", (int) Math.round(target.getSolution())) + "%", ROW_0_X, TARGET_Y + ROW_HEIGHT * 3);
			
			g2d.drawString("HDG  "+ Magnitudes.radiansToHumanDegrees(target.getHeading()), ROW_1_X, TARGET_Y + ROW_HEIGHT * 1);
			g2d.drawString("SPD   "+ Magnitudes.knotsToHuman(target.getSpeed()) + " kn", ROW_1_X, TARGET_Y + ROW_HEIGHT * 2);
		}
	}
	
	@Override
	public void tick() {
		
	}

	/* (non-Javadoc)
	 * @see commons.Screen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
