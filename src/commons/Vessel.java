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
	private double maxSpeedReverse;
	/** Maximum possible speed */
	private double maxSpeed;
	/** Cruise speed, maximum turning capability */
	private double standardSpeed;
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
		if (speed > maxSpeed || speed < maxSpeedReverse) {
			speed = (speed > 0) ? maxSpeed : maxSpeedReverse;
		} else if (speed != mySpeed) {
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
		//Speed here is in knots
		
		if (heading != myHeading) {
			double angleDiff = (2 * Math.PI + myHeading - heading) % (2 * Math.PI);
			//Determine left or right
			int rotDir = (angleDiff < Math.PI) ? 1 : -1;
			//Determine turning coefficient based on speed
			double rotCof = Math.abs(speed) / standardSpeed;
			if (rotCof > 1) {
				rotCof = 1;
			}
			double deltaRot = rotationSpeed * Clock.TICK_TIME * rotDir * rotCof;
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
		//Speed here is converted to feet/s
		double longitude = position.getX()
				+ speed * Magnitudes.FEET_SECOND_PER_KN * Math.sin(heading) * Clock.TICK_TIME;
		double latitude = position.getY()
				+ speed * Magnitudes.FEET_SECOND_PER_KN * Math.cos(heading) * Clock.TICK_TIME;
		position.setLocation(longitude, latitude);
	}
	
	/**
	 * @param other the other vessel
	 * @return the distance between this and the other vessel
	 */
	public double distanceTo(Vessel other) {
		return this.position.distance(other.getPosition());
	}
	
	/**
	 * @param other the other vessel
	 * @return the bearing to the other vessel from this vessel, against the Y axis
	 */
	public double bearingTo(Vessel other) {
		Point2D relPos = relativePositionOf(other);
		// Since the angle is measured against the Y axis I can't use the Math.atan2
		// function
		// Angle is measured using the arc cosine of the Y component
		// and substracted from 360º if the X component is negative
		double bearing = Math.acos(relPos.getY());
		if (relPos.getX() < 0) {
			bearing = 2 * Math.PI - bearing;
		}
		return bearing;
	}
	
	/**
	 * @param other the other vessel
	 * @return the position of the other vessel with this vessel as origin
	 */
	public Point2D relativePositionOf(Vessel other) {
		Point2D relativePosition = new Point2D.Double(
				other.getPosition().getX() - this.position.getX(),
				other.getPosition().getY() - this.position.getY()
				);
		return relativePosition;
	}

	/**
	 * @return the mySpeed
	 */
	public double getMySpeed() {
		return mySpeed;
	}

	/**
	 * @param mySpeed the mySpeed to set
	 */
	public void setMySpeed(double mySpeed) {
		this.mySpeed = mySpeed;
	}

	/**
	 * @return the maxSpeed
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * @return the acceleration
	 */
	public double getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * @return the myHeading
	 */
	public double getMyHeading() {
		return myHeading;
	}

	/**
	 * @param myHeading the myHeading to set
	 */
	public void setMyHeading(double myHeading) {
		this.myHeading = myHeading;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return the heading
	 */
	public double getHeading() {
		return heading;
	}

	/**
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Point2D position) {
		this.position = position;
	}

	/**
	 * @return the solution
	 */
	public double getSolution() {
		return solution;
	}

	/**
	 * @return the rotationSpeed
	 */
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @param rotationSpeed the rotationSpeed to set
	 */
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * @return the standardSpeed
	 */
	public double getStandardSpeed() {
		return standardSpeed;
	}

	/**
	 * @param standardSpeed the standardSpeed to set
	 */
	public void setStandardSpeed(double standardSpeed) {
		this.standardSpeed = standardSpeed;
	}

	/**
	 * @return the maxSpeedReverse
	 */
	public double getMaxSpeedReverse() {
		return maxSpeedReverse;
	}

	/**
	 * @param maxSpeedReverse the maxSpeedReverse to set
	 */
	public void setMaxSpeedReverse(double maxSpeedReverse) {
		this.maxSpeedReverse = maxSpeedReverse;
	}
	
	

}
