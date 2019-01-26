package ships;

import commons.Vessel;

/**
*@author Sebas Lavigne
*
*/

public abstract class Ship extends Vessel {
	
	protected int state;
	
	/* Solution and detection between 0 and 1 */
	/** The player's solution on this ship */
	protected double solution;
	/** This ships's solution on the player */
	protected double detection;
	
	/** The sonar listening capacity for detecting the sub */
	protected double sonarQuality;
	
	public Ship() {
		super();
	}
	
	public void computeSolution() {
		
	}
	
	public void computeDetection() {
		
	}

}

