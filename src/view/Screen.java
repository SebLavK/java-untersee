package view;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
*@author Sebas Lavigne
*/

public interface Screen {

	public void initializeScreen();
	
	public void drawScreen(Graphics g);
	
	public void tick();
	
	public void mouseClicked(MouseEvent e);
	
}
