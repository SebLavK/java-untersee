package view.screens;

import view.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
*@author Sebas Lavigne
*/

public class BlankScreen implements Screen{
	
	private JPanel panel;
	
	public BlankScreen(JPanel panel) {
		this.panel = panel;
	}
	
	

	@Override
	public void initializeScreen() {
		
	}

	/* (non-Javadoc)
	 * @see view.Screen#drawScreen(java.awt.Graphics)
	 */
	@Override
	public void drawScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
	}

	/* (non-Javadoc)
	 * @see view.Screen#tick()
	 */
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see view.Screen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
