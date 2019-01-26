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
	
	private int tickCount;
	private Scenario scenario;
	private GamePanel gamePanel;
	private ExecutiveOfficer xo;
	
	public Master(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void initializeMaster() {
		ImageResource.instantiateImages();
		scenario = new Scenario();
		
		xo = new ExecutiveOfficer(scenario.getSub());
		xo.initialize();
		
		gamePanel.setCurrentScreen(new MapScreen(this, gamePanel));
		Clock.setGameStartTime();
		new Thread(this).start();
	}
	
	private void tick() {
		scenario.tick();
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
			long waitTimeMillis = (Clock.FRAME_NANO - elapsedTime) / 1000000 - 1;
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
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * @return the tickCount
	 */
	public int getTickCount() {
		return tickCount;
	}

	
}
