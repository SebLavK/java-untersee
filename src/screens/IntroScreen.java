package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import commons.Clock;
import commons.ImageResource;
import commons.Screen;
import master.Master;

/**
*@author Sebas Lavigne
*/

public class IntroScreen implements Screen {
	
	private Master master;
	
	private BufferedImage picture;
	
	private boolean fadeOut;
	private boolean finished;
	private int startFade;
	
	

	public IntroScreen(Master master) {
		super();
		this.master = master;
	}

	@Override
	public void initializeScreen() {
		picture = ImageResource.getIntroImage();
		fadeOut = false;
		finished = false;
	}

	/* (non-Javadoc)
	 * @see commons.Screen#drawScreen(java.awt.Graphics)
	 */
	@Override
	public void drawScreen(Graphics g) {
		g.drawImage(picture, 0, 0, null);
		if (fadeOut) {
			int transparency = (int) Math.round(
					((double)Clock.getTickCount() - startFade) / (Clock.FPS) * 255
					);
			g.setColor(new Color(0, 0, 0,
					transparency
					));
			g.fillRect(0, 0, 800, 800);
			if (transparency >= 255) {
				finished = true;
			}
		}
		if (finished) {
			master.startRunning();
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
		fadeOut = true;
		startFade = Clock.getTickCount();
	}
	

}
