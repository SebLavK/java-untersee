package master;

import java.awt.Toolkit;
import java.awt.geom.Point2D;

import commons.Clock;
import main.GamePanel;
import screens.MapScreen;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class Master implements Runnable {
	
	private Submarine sub;
	private GamePanel gamePanel;
	private ExecutiveOfficer xo;
	
	public Master(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void initializeMaster() {
		sub = new Submarine();
		sub.setAcceleration(0.1);
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(0);
		sub.setMaxSpeed(2);
		sub.setPosition(new Point2D.Double(100, -100));
		sub.setRotationSpeed(0.5);
		
		xo = new ExecutiveOfficer(sub);
		xo.initialize();
		
		((MapScreen) gamePanel.getCurrentScreen()).setSub(sub);
		new Thread(this).start();
	}
	
	private void tick() {
		try {
			Thread.sleep(Clock.FRAME_PERIOD);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sub.tick();
		gamePanel.repaint();
		//Para funcionamiento fluido en Linux
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void run() {
		while(true) {
			tick();
		}
	}

}
