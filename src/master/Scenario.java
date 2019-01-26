package master;

import java.awt.geom.Point2D;
import java.util.HashSet;

import ships.Battleship;
import ships.Ship;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class Scenario {
	
	private Camera camera;
	private Submarine sub;
	private HashSet<Ship> ships;

	public Scenario() {
		initializeSub();
		initializeCamera();
		populateShips();
	}
	
	public void initializeSub() {
		sub = new Submarine();
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(0);
		sub.setSpeed(0);
		sub.setPosition(new Point2D.Double(0, 0));
	}
	
	public void initializeCamera() {
		camera = new Camera(sub);
		camera.setPosition(new Point2D.Double(0,0));
	}
	
	public void populateShips() {
		ships = new HashSet<>();
		
		Battleship b = new Battleship();
		b.setPosition(new Point2D.Double(100, 100));
		
		ships.add(b);
	}
	
	public void tick() {
		sub.tick();
		for (Ship ship : ships) {
			ship.tick();
		}
		
		camera.tick();
	}
	
	

	/**
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @return the sub
	 */
	public Submarine getSub() {
		return sub;
	}

	/**
	 * @return the ships
	 */
	public HashSet<Ship> getShips() {
		return ships;
	}

}
