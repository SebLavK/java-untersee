package main;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import commons.Screen;
import screens.MapScreen;

/**
*@author Sebas Lavigne
*/

public class GamePanel extends JPanel {
	
	private Screen currentScreen;
	
	public void initializePanel() {
		currentScreen = new MapScreen(this);
		currentScreen.initializeScreen();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		currentScreen.drawScreen(g);
	}

	/**
	 * @return the currentScreen
	 */
	public Screen getCurrentScreen() {
		return currentScreen;
	}

	/**
	 * @param currentScreen the currentScreen to set
	 */
	public void setCurrentScreen(Screen currentScreen) {
		this.currentScreen = currentScreen;
	}
	
	


}
