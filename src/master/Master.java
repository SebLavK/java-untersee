package master;

import java.awt.Toolkit;
import java.awt.geom.Point2D;

import commons.Clock;
import commons.ImageResource;
import main.GamePanel;
import screens.MapScreen;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class Master implements Runnable {
	
	public static int tickCount;
	
	private Submarine sub;
	private GamePanel gamePanel;
	private ExecutiveOfficer xo;
	
	public Master(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void initializeMaster() {
		ImageResource.instantiateImages();
		sub = new Submarine();
		sub.setAcceleration(0.1);
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(0);
		sub.setMaxSpeed(Submarine.SPEED_FLANK);
		sub.setStandardSpeed(Submarine.SPEED_STANDARD);
		sub.setMaxSpeedReverse(Submarine.SPEED_BACK_EMERG);
		sub.setPosition(new Point2D.Double(100, -100));
		sub.setRotationSpeed(0.5);
		
		xo = new ExecutiveOfficer(sub);
		xo.initialize();
		
		gamePanel.setCurrentScreen(new MapScreen(this, gamePanel));
		
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
		tickCount++;
	}

	@Override
	public void run() {
		while(true) {
			tick();
		}
	}

	/**
	 * @return the sub
	 */
	public Submarine getSub() {
		return sub;
	}

}
