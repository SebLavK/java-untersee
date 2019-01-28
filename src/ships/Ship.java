package ships;

import commons.Vessel;

/**
*@author Sebas Lavigne
*
*/

public abstract class Ship extends Vessel {
	
	protected int state;
	
	
	
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

