package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import commons.Screen;

/**
*@author Sebas Lavigne
*/

public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Screen currentScreen;
	
	public void initializePanel() {
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				currentScreen.mouseClicked(e);
			}
		});
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
