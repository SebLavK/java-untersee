package commons;

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;

/**
*@author Sebas Lavigne
*/

public abstract class Vessel {
	
	private int behaviourMode;
	
	/* Vessel magnitudes */
	private double beam;
	private double length;
	
	/* Vessel capabilities */
	/** Desired speed */
	private double mySpeed;
	/** Current speed */
	private double speed;
	/** Maximum possible speed */
	private double maxSpeed;
	private double acceleration;
	/* Heading should be 0 if going north, negative Y */
	/** Desired heading */
	private double myHeading;
	/** Current heading */
	private double heading;
	private double rotationSpeed;
	
	/* Position */
	/** Current position, X is East, Y is North */
	private Point2D position;
	/** A collection of points that define this vessel's course */
	private LinkedHashSet<Point2D> course;
	
	/* Solution and detection between 0 and 1 */
	/** The player's solution on this ship */
	private double solution;
	/** This ships's solution on the player */
	private double detection;
	
	public void tick() {
		steer();
		sail();
	}
	
	/**
	 * Changes speed and heading acording to settings
	 */
	private void steer() {
		if (speed != mySpeed) {
			//Forwards or reverse
			int dir = (speed < mySpeed) ? 1 : -1;
			//Amount to increase speed
			double deltaSpeed = acceleration * Clock.TICK_TIME * dir;
			//If accelerating overshoots the desired speed
			if (Math.abs(deltaSpeed) > Math.abs(mySpeed - speed)) {
				//Accelerate what needed
				speed = mySpeed;
			} else {
				//Accelerate what calculated
				speed += deltaSpeed;
			}
		}
		
		if (heading != myHeading) {
			double angleDiff = (2 * Math.PI + myHeading - heading) % (2 * Math.PI);
			//Determine left or right
			int rotDir = (angleDiff < Math.PI) ? 1 : -1;
			double deltaRot = rotationSpeed * Clock.TICK_TIME * rotDir;
			//If turning overshoots the desired heading
			if (Math.abs(deltaRot) > Math.abs(angleDiff)) {
				heading = myHeading;
			} else {
				heading += deltaRot;
			}
		}
	}
	
	/**
	 * Changes position according to speed and heading
	 */
	private void sail() {
		//Current position + deltaPosition
		double longitude = position.getX() + speed * Math.sin(heading);
		double latitude = position.getY() + speed * Math.cos(heading);
		position.setLocation(longitude, latitude);
	}

}
