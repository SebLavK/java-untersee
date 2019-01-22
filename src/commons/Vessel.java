package commons;

/**
*@author Sebas Lavigne
*/

public abstract class Vessel {
	
	/* Vessel magnitudes */
	private float beam;
	private float length;
	
	/* Vessel capabilities */
	/** Desired speed */
	private float mySpeed;
	/** Current speed */
	private float speed;
	/** Maximum possible speed */
	private float maxSpeed;
	private float acceleration;
	/** Desired heading */
	private float myHeading;
	/** Current heading */
	private float heading;
	private float rotationSpeed;
	
	/* Between 0 and 1 */
	/** The player's solution on this ship */
	private float solution;
	/** This ships's solution on the player */
	private float detection;
	
	public void tick() {
		
	}
	
	/**
	 * 
	 */
	private void steer() {
		
	}

}
