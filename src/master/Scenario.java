package master;

import java.awt.geom.Point2D;
import java.util.HashSet;

import ships.Ship;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class Scenario {
	
	private Submarine sub;
	private HashSet<Ship> ships;

	public Scenario() {
		ships = new HashSet<>();
		sub = new Submarine();
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(20);
		sub.setSpeed(20);
		sub.setPosition(new Point2D.Double(0, 0));
	}
	
	public void tick() {
		sub.tick();
		for (Ship ship : ships) {
			ship.tick();
		}
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
