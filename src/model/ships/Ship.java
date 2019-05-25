package model.ships;

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
		acceleration = 0.7;
		rotationSpeed = 0.03;
		maxSpeed = 35;
		maxSpeedReverse = -16;
		standardSpeed = 20;
		maxDepth = 0;
		minDepth = 0;
		depthBubble = 0;
		
	}
	
	public void computeSolution() {
		
	}
	
	public void computeDetection() {
		
	}

}

