package master;

import commons.Vessel;
import ships.*;
import submarine.Submarine;
import weapons.Projectile;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
*@author Sebas Lavigne
*/

public class Scenario {
	
	private Camera camera;
	private Submarine sub;
	private HashSet<Ship> ships;
	private HashSet<Projectile> projectiles;

	public Scenario() {
		initializeSub();
		initializeCamera();
		populateShips();
		projectiles = new HashSet<>();
	}
	
	public void initializeSub() {
		sub = new Submarine(this);
		sub.setMyHeading(Math.toRadians(0));
		sub.setMySpeed(0);
		sub.setSpeed(0);
		sub.setMyDepth(Submarine.PERISCOPE_DEPTH);
		sub.setPosition(new Point2D.Double(0, 0));
	}
	
	public void initializeCamera() {
		camera = new Camera(sub);
		camera.setPosition(sub.getPosition());
	}
	
	public void populateShips() {
		ships = new HashSet<>();
		Random rd = new Random();
		
		Battleship b = new Battleship();
		b.setPosition(new Point2D.Double(100, 100));
		
		Cruiser c = new Cruiser();
		c.setPosition(new Point2D.Double(-500 , 200));
		c.setMyHeading(60);
		c.setMySpeed(15);
		
		PatrolShip p = new PatrolShip();
		p.setPosition(new Point2D.Double(-1000 , -200));
		p.setMyHeading(340);
		p.setMySpeed(20);
		
		Destroyer d = new Destroyer();
		d.setPosition(new Point2D.Double(rd.nextDouble() * 2000 - 1000, rd.nextDouble() * 2000 - 1000));
		d.setMyHeading(180);
		d.setMySpeed(10);
		
		RescueShip r = new RescueShip();
		r.setPosition(new Point2D.Double(rd.nextDouble() * 2000 - 1000, rd.nextDouble() * 2000 - 1000));
		r.setMySpeed(15);
		
		ships.add(b);
		ships.add(c);
		ships.add(p);
		ships.add(d);
		ships.add(r);
	}
	
	public void tick() {
		sub.tick();
		ships.forEach(Vessel::tick);
		projectiles.forEach(Vessel::tick);
		removeDestroyed();
		
		camera.tick();
	}
	
	public void removeDestroyed() {
		ships.removeIf(Vessel::isDestroyed);
		projectiles.removeIf(Vessel::isDestroyed);
	}

	/**
	 * @return the projectiles
	 */
	public Set<Projectile> getProjectiles() {
		return projectiles;
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
	public Set<Ship> getShips() {
		return ships;
	}

}
