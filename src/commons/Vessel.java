package commons;

import master.Scenario;

import java.awt.geom.Point2D;
import java.util.LinkedHashSet;

/**
*@author Sebas Lavigne
*/

public abstract class Vessel {
	
	/* Vessel capabilities */
	/** Desired speed */
	protected double mySpeed;
	/** Current speed */
	protected double speed;
	protected double maxSpeedReverse;
	/** Maximum possible speed */
	protected double maxSpeed;
	/** Cruise speed, maximum turning capability */
	protected double standardSpeed;
	protected double acceleration;
	/* Heading should be 0 if going north, negative Y */
	/** Desired heading */
	protected double myHeading;
	/** Current heading */
	protected double heading;
	protected double rotationSpeed;
	/** Depth */
	protected double depth;
	protected double myDepth;
	protected double maxDepth;
	protected double minDepth;
	/* Rate of diving by changing buoyancy */
	protected double depthBubble;
	
	protected String designation;
	
	/* Solution and detection between 0 and 1 */
	/** The player's solution on this ship */
	protected double solution;
	/** This ships's solution on the player */
	protected double detection;
	
	/* Position */
	/** Current position, X is East, Y is North */
	protected Point2D position;
	/** A collection of points that define this vessel's course */
	protected LinkedHashSet<Point2D> course;
	
	protected Vessel target;
	protected boolean goToTarget;
	
	protected boolean destroyed;
	
	protected Scenario scenario;
	
	
	public Vessel() {
		super();
		designation = "";
	}

	public void tick() {
		makeTurns();
		steer();
		dive();
		sail();
		if (goToTarget) {
			steerTowardsTarget();
		}
	}
	
	/**
	 * Changes speed according to settings
	 */
	protected void makeTurns() {
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
	}
	
	/**
	 * Changes heading according to settings
	 */
	protected void steer() {
		if (heading != myHeading) {
			double angleDiff = (2 * Math.PI + myHeading - heading) % (2 * Math.PI);
			//Determine left or right
			int rotDir = (angleDiff < Math.PI) ? 1 : -1;
			//Determine turning coefficient based on speed and desired speed, will turn slower if below 1/3 speed
			double rotCof = 1; //base rotation
			//bonus due to speed (more force by rudder)
			double rotRudder = (Math.abs(speed) < standardSpeed / 3) ?
					-1 + Math.abs(speed) / (standardSpeed / 3) :
					(Math.abs(speed) - standardSpeed / 3) / (standardSpeed / 3);
			double rotPropeller =Math.abs(mySpeed - speed) / maxSpeed;
			rotCof += rotRudder + rotPropeller;
//			System.out.println(speed + "\t" + rotCof + "\t" + rotRudder + "\t" + rotPropeller);
			if (Double.isNaN(rotCof)) {
				rotCof = 0;
			}
			double deltaRot = rotationSpeed * Clock.TICK_TIME * rotDir * rotCof;
			//If turning overshoots the desired heading
			if (Math.abs(deltaRot) > Math.abs(angleDiff)) {
				heading = myHeading;
			} else {
				heading += deltaRot;
			}
			if (heading < 0) {
				heading += Math.PI * 2;
			}
		}
	}

	/**
	 * Changes depth according to settings
	 */
	protected void dive() {
		if (depth > maxDepth || depth < minDepth) {
			depth = (depth > maxDepth) ? maxDepth : minDepth;
		} else if (depth != myDepth) {
			//Down or up
			int dir = (depth < myDepth) ? 1 : -1;
			//Depth change rate depending on speed, up to 5 ft/s at max speed
			double depthPlane = Math.abs(speed) / maxSpeed * 5;
			//Amount to increase depth
			double deltaDepth = (depthBubble + depthPlane) * Clock.TICK_TIME * dir;
			//If diving overshoots desired depth
			if (Math.abs(deltaDepth) > Math.abs(myDepth - depth)) {
				depth = myDepth;
			} else {
				depth += deltaDepth;
			}
		}
	}
	
	/**
	 * Changes position according to speed and heading
	 */
	protected void sail() {
		//Speed here is converted to feet/s
		double longitude = position.getX()
				+ speed * Magnitudes.FEET_SECOND_PER_KN * Math.sin(heading) * Clock.TICK_TIME;
		double latitude = position.getY()
				+ speed * Magnitudes.FEET_SECOND_PER_KN * Math.cos(heading) * Clock.TICK_TIME;
		position.setLocation(longitude, latitude);
	}
	
	protected void steerTowardsTarget() {
		myHeading = bearingTo(target);
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
		double bearing = Math.atan2(relPos.getX(), relPos.getY());
		if (bearing < 0) {
			bearing += 2 * Math.PI;
		}
		return bearing;
	}
	
	/**
	 * @param other the other vessel
	 * @return the position of the other vessel with this vessel as origin
	 */
	public Point2D relativePositionOf(Vessel other) {
		return new Point2D.Double(
				other.getPosition().getX() - this.position.getX(),
				other.getPosition().getY() - this.position.getY()
				);
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
	 * @return the acceleration
	 */
	public double getAcceleration() {
		return acceleration;
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
	 * @return the rotationSpeed
	 */
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @return the standardSpeed
	 */
	public double getStandardSpeed() {
		return standardSpeed;
	}

	/**
	 * @return the maxSpeedReverse
	 */
	public double getMaxSpeedReverse() {
		return maxSpeedReverse;
	}

	/**
	 * @return the myDepth
	 */
	public double getMyDepth() {
		return myDepth;
	}

	/**
	 * @param myDepth the myDepth to set
	 */
	public void setMyDepth(double myDepth) {
		this.myDepth = myDepth;
	}

	/**
	 * @return the depth
	 */
	public double getDepth() {
		return depth;
	}

	/**
	 * @return the maxDepth
	 */
	public double getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @return the minDepth
	 */
	public double getMinDepth() {
		return minDepth;
	}

	/**
	 * @return the depthBubble
	 */
	public double getDepthBubble() {
		return depthBubble;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the target
	 */
	public Vessel getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Vessel target) {
		this.target = target;
	}

	/**
	 * @return the destroyed
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * @param destroyed the destroyed to set
	 */
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	/**
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * @return the solution
	 */
	public double getSolution() {
		return solution;
	}

	/**
	 * @return the detection
	 */
	public double getDetection() {
		return detection;
	}

	
	

}
