package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import commons.ImageResource;
import commons.Magnitudes;
import commons.Screen;
import main.GamePanel;
import master.Master;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class DataScreen implements Screen {
	
	public static final int NAV_Y = 20;
	public static final int WEAPONS_Y = 120;
	public static final int TARGET_Y = 210;
	public static final int ROW_HEIGHT = 24;
	public static final int HEAD_X = 50;
	public static final int HEAD_LINE_MARGIN = 20;
	public static final int HEAD_LINE_HEIGHT = 3;
	public static final int ROW_0_X = 20;
	public static final int ROW_1_X = 20;
	public static final int ROW_2_X = 30;
	
	public static final int FONT_BIG_SIZE = 16;
	public static final int FONT_SMALL_SIZE = 16;
	
	public static final Color BG_COLOR = Color.BLACK;
	public static final Color TEXT_COLOR = Color.WHITE;
	public static final Color HEAD_COLOR = new Color(0, 127, 127);

	private Master master;
	private GamePanel dataPanel;
	
	private BufferedImage bg;
	
	private Font bigFont;
	private Font smallFont;
	
	
	public DataScreen(Master master, GamePanel dataPanel) {
		this.master = master;
		this.dataPanel = dataPanel;
		initializeScreen();
	}

	@Override
	public void initializeScreen() {
		bigFont = ImageResource.getMainFont();
		smallFont = ImageResource.getMainFont();
		bigFont = bigFont.deriveFont(Font.PLAIN, FONT_BIG_SIZE);
		smallFont = smallFont.deriveFont(Font.PLAIN, FONT_SMALL_SIZE);
	}
	
	@Override
	public void drawScreen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		drawSections(g2d);
		drawNavigation(g2d);
		
		g2d.dispose();
		g.dispose();
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
		
		g2d.drawString("HDG  "+ Magnitudes.radiansToHumanDegrees(sub.getHeading()), ROW_0_X, NAV_Y + ROW_HEIGHT * 1);
		g2d.drawString("SPD   "+ Magnitudes.knotsToHuman(sub.getSpeed()) + " kn", ROW_0_X, NAV_Y + ROW_HEIGHT * 2);
		g2d.drawString("DEP "+ Magnitudes.feetToHuman(sub.getDepth()) + " ft", ROW_0_X, NAV_Y + ROW_HEIGHT * 3);
		
	}
	
	@Override
	public void tick() {
		
	}
}
