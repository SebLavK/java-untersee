package master;

import java.awt.geom.Point2D;
import java.util.HashSet;

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
		ships = new HashSet<>();
		sub = new Submarine();
		camera = new Camera(sub);
		camera.setPosition(new Point2D.Double(0,0));
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(0);
		sub.setSpeed(0);
		sub.setPosition(new Point2D.Double(0, 0));
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
