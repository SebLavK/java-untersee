package commons;

import java.awt.Graphics;

/**
*@author Sebas Lavigne
*/

public interface Screen {

	public void initializeScreen();
	
	public void drawScreen(Graphics g);
	
	public void tick();
	
}
