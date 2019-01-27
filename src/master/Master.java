package master;

import java.awt.Toolkit;

import commons.Clock;
import commons.ImageResource;
import main.GamePanel;
import main.SidePanel;
import screens.DataScreen;
import screens.MapScreen;

/**
*@author Sebas Lavigne
*/

public class Master implements Runnable {
	
	public static Master master;
	
	private int tickCount;
	private Scenario scenario;
	private GamePanel gamePanel;
	private SidePanel sidePanel;
	private ExecutiveOfficer xo;
	
	public Master(GamePanel gamePanel, SidePanel sidePanel) {
		this.gamePanel = gamePanel;
		this.sidePanel = sidePanel;
		master = this;
	}
	
	public void initializeMaster() {
		ImageResource.instantiateImages();
		scenario = new Scenario();
		
		xo = new ExecutiveOfficer(this, scenario.getSub());
		xo.initialize();
		
		gamePanel.setCurrentScreen(new MapScreen(this, gamePanel));
		sidePanel.setCurrentScreen(new DataScreen(this, sidePanel.getDataPanel()));
	}
	
	public void startGame() {
		Clock.setGameStartTime();
		new Thread(this).start();
	}
	
	private void tick() {
		scenario.tick();
		gamePanel.repaint();
		sidePanel.repaint();
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
	
	/**
	 * @return the sidePanel
	 */
	public SidePanel getSidePanel() {
		return sidePanel;
	}

	/**
	 * @return the xo
	 */
	public ExecutiveOfficer getXo() {
		return xo;
	}
	
	

	
}
