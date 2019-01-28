package submarine;

import java.util.HashSet;

import commons.Clock;
import commons.Magnitudes;
import commons.Vessel;
import master.ExecutiveOfficer;
import master.Scenario;
import ships.Ship;

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
				ExecutiveOfficer.log("Sonar:  New contact bearing " + bearing + "º. Designated "+ship.getDesignation() + ".");
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
			ExecutiveOfficer.log("Sonar:  No targets with that designation.");
		}
	}

	/**
	 * @return the contacts
	 */
	public HashSet<Ship> getContacts() {
		return contacts;
	}
	
	

}
