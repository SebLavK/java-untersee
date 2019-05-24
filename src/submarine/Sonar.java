package submarine;

import commons.Clock;
import commons.Magnitudes;
import commons.Vessel;
import commons.gameObject.Verbose;
import master.ExecutiveOfficer;
import master.Scenario;
import ships.Ship;

import java.util.HashSet;

/**
*@author Sebas Lavigne
*/

public class Sonar {
	
	private Scenario scenario;
	private Submarine sub;
	private HashSet<Ship> contacts;
	private int designationTicket;
	
	public Sonar(Scenario scenario, Submarine sub) {
		this.scenario = scenario;
		this.sub = sub;
		contacts = new HashSet<>();
	}
	
	public void tick() {
		everyFiveSeconds();
	}
	
	public void everyFiveSeconds() {
		if (Clock.getTickCount() % (Clock.FPS * 5) == 0) {
			updateContacts();
			removeDestroyed();
		}
	}
	
	public void updateContacts() {
		HashSet<Ship> ships = scenario.getShips();
		//TODO make this realistic, only save contacts that can be heard by sonar
		for (Ship ship : ships) {
			if (!contacts.contains(ship)) {
				designationTicket++;
				ship.setDesignation("S"+designationTicket);
				contacts.add(ship);
				String bearing = Magnitudes.radiansToHumanDegrees(sub.bearingTo(ship));
				ExecutiveOfficer.log(new Verbose("header.sonar",
						"update.sonar.new.contact",
						new String[]{bearing, ship.getDesignation()}));
			}
		}
	}
	
	public void removeDestroyed() {
		HashSet<Vessel> toRemove = new HashSet<>();
		for (Ship ship : contacts) {
			if (ship.isDestroyed()) {
				toRemove.add(ship);
			}
		}
		
		for (Vessel vessel : toRemove) {
			if (contacts.contains(vessel)) {
				contacts.remove(vessel);
			}
		}
	}
	
	public void target(String designation) {
		boolean found = false;
		for (Ship ship : contacts) {
			if (ship.getDesignation().equalsIgnoreCase(designation)) {
				sub.setTarget(ship);
				found = true;
			}
		}
		if (!found) {
			ExecutiveOfficer.log(new Verbose("header.sonar",
					"reply.sonar.unknown.contact"));
		}
	}

	/**
	 * @return the contacts
	 */
	public HashSet<Ship> getContacts() {
		return contacts;
	}
	
	

}
