package model.weapons;

import model.ships.Vessel;

import java.awt.geom.Point2D;

/**
*@author Sebas Lavigne
*/

public class Torpedo extends Projectile {
	
	public static final double LAUNCH_SPEED = 3;
	public static final double TORPEDO_SLOW = 20;
	public static final double TORPEDO_FAST = 40;
	public static final double BLAST_RADIUS = 30;
	
	public static final double ACCELERATION = 1;
	public static final double TURNING_SPEED = 0.08;
	public static final double MIN_DEPTH = 0;
	public static final double MAX_DEPTH = 2000;
	public static final double DEPTH_BUBBLE = 0;
	
	
	public Torpedo(Vessel owner, double mySpeed, Vessel target) {
		this.speed = owner.getSpeed() + LAUNCH_SPEED;
		this.heading = owner.getHeading();
		this.position = new Point2D.Double(owner.getPosition().getX(), owner.getPosition().getY());
		this.mySpeed = mySpeed;
		this.target = target;
		
		goToTarget = true;
		maxSpeed = TORPEDO_FAST;
		maxSpeedReverse = - TORPEDO_SLOW;
		acceleration = ACCELERATION;
		standardSpeed = TORPEDO_SLOW;
		rotationSpeed = TURNING_SPEED;
		minDepth = MIN_DEPTH;
		maxDepth = MAX_DEPTH;
		depthBubble = DEPTH_BUBBLE;
	}
	
	
	
	@Override
	public void tick() {
		super.tick();
		checkExplode();
	}

	public void checkExplode() {
		if (distanceTo(target) < BLAST_RADIUS) {
			target.setDestroyed(true);
			this.destroyed = true;
		}
	}
	
	public void reacquireTarget() {
		
	}
	
	

}
