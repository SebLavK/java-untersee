package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import commons.Screen;

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
	 * @see commons.Screen#drawScreen(java.awt.Graphics)
	 */
	@Override
	public void drawScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
	}

	/* (non-Javadoc)
	 * @see commons.Screen#tick()
	 */
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see commons.Screen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
