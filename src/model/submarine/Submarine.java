package model.submarine;

import model.Scenario;
import model.ships.Vessel;

/**
*@author Sebas Lavigne
*/

public class Submarine extends Vessel {
	
	public static final double SPEED_FLANK = 35;
	public static final double SPEED_FULL = 20;
	public static final double SPEED_STANDARD = 15;
	public static final double SPEED_BACK_EMERG = -16;
	public static final double SPEED_BACK_FULL = -12;
	
	public static final double ACCELERATION = 0.5;
	
	public static final double TURNING_RATE = 0.05235987755982988;
	
	public static final double TEST_DEPTH = 950;
	public static final double CRUSH_DEPTH = 1450;
	public static final double PERISCOPE_DEPTH = 63;
	public static final double DEPTH_BUBBLE = 2;
	public static final double SAIL_DEPTH = 46;
	public static final double SURFACE_DEPTH = 27;
	
	public static final String DEFAULT_NAME = "Stingray";
	
	private Helm helm;
	private Sonar sonar;
	private TorpedoRoom tRoom;
	
	private double periscopeDepth;
	
	public Submarine(Scenario scenario) {
		super();
		this.scenario = scenario;
		helm = new Helm(this);
		sonar = new Sonar(scenario, this);
		tRoom = new TorpedoRoom(this);
		
		maxSpeed = SPEED_FLANK;
		standardSpeed = SPEED_STANDARD;
		maxSpeedReverse = SPEED_BACK_EMERG;
		
		acceleration = ACCELERATION;
		
		rotationSpeed = TURNING_RATE;
		
		maxDepth = TEST_DEPTH;
		minDepth = SURFACE_DEPTH;
		periscopeDepth = PERISCOPE_DEPTH;
		depthBubble = DEPTH_BUBBLE;
		depth = periscopeDepth;
		
		designation = DEFAULT_NAME;
	}

	@Override
	public void tick() {
		super.tick();
		helm.tick();
		sonar.tick();
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the sonar
	 */
	public Sonar getSonar() {
		return sonar;
	}

	/**
	 * @return the tRoom
	 */
	public TorpedoRoom gettRoom() {
		return tRoom;
	}
	

	
}
