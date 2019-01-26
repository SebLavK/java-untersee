package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import commons.Screen;

/**
*@author Sebas Lavigne
*/

public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Screen currentScreen;
	
	public void initializePanel() {
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
