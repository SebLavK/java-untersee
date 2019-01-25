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
	/** Zoom amount. The bigger the further away */
	public static int mapZoom = 1;
	
	private Submarine sub;
	private GamePanel gamePanel;
	private ExecutiveOfficer xo;
	
	public Master(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void initializeMaster() {
		ImageResource.instantiateImages();
		sub = new Submarine();
		sub.setAcceleration(0.5);
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(35);
		sub.setMaxSpeed(Submarine.SPEED_FLANK);
		sub.setStandardSpeed(Submarine.SPEED_STANDARD);
		sub.setMaxSpeedReverse(Submarine.SPEED_BACK_EMERG);
		sub.setPosition(new Point2D.Double(0, 0));
		sub.setRotationSpeed(Math.toRadians(3));
		
		xo = new ExecutiveOfficer(sub);
		xo.initialize();
		
		gamePanel.setCurrentScreen(new MapScreen(this, gamePanel));
		Clock.setGameStartTime();
		new Thread(this).start();
	}
	
	private void tick() {
		sub.tick();
		gamePanel.repaint();
		//Para funcionamiento fluido en Linux
		Toolkit.getDefaultToolkit().sync();
		tickCount++;
	}

	@Override
	public void run() {
		while (true) {
			long tickStartTime = Clock.getCurrentTime();
			tick();
			long elapsedTime = Clock.timeSince(tickStartTime);
			long waitTimeMillis = Clock.FRAME_PERIOD - 1 - elapsedTime / 1000000;
			int waitTimeNanos = 1000000 - (int) (elapsedTime % 1000000);
			if (waitTimeMillis < 0) {
				waitTimeMillis = 0;
				waitTimeNanos = 0;
			}
			try {
				Thread.sleep(waitTimeMillis, waitTimeNanos);
//				Thread.sleep(Clock.FRAME_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the sub
	 */
	public Submarine getSub() {
		return sub;
	}

}
