package submarine;

import weapons.TorpedoTube;

/**
*@author Sebas Lavigne
*/

/**
 * The torpedo tubes of the player's submarine are special compared to the normal torpedo tube.
 * The player must load, flood and open the tubes in order to fire them
 */
public class TorpedoRoom {
	/*
	 * Ratios from 0 to 1, speed in percentiles per second
	 */
	private Submarine sub;
	private TorpedoTube[] tubes;
	private double[] loadRatio;
	private double loadSpeed;
	private double[] floodRatio;
	private double floodSpeed;
	private double[] openRatio;
	private double openSpeed;
	
	
	public TorpedoRoom(Submarine sub) {
		super();
		this.sub = sub;
	}
	
	public void launch(int tubeNum) {
		tubes[tubeNum].setTarget(sub.getTarget());
		tubes[tubeNum].fire();
	}

}
