package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import commons.Screen;
import main.GamePanel;
import master.Master;

/**
*@author Sebas Lavigne
*/

public class DataScreen implements Screen {

	private Master master;
	private GamePanel dataPanel;
	
	private BufferedImage bg;
	
	
	
	public DataScreen(Master master, GamePanel dataPanel) {
		this.master = master;
		this.dataPanel = dataPanel;
	}

	@Override
	public void initializeScreen() {
		bg = new BufferedImage(dataPanel.getWidth(), dataPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	@Override
	public void drawScreen(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
	}
	
	@Override
	public void tick() {
		
	}
}
