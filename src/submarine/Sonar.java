package submarine;

import java.util.HashSet;

import commons.Clock;
import commons.Vessel;
import master.Scenario;
import ships.Ship;

/**
*@author Sebas Lavigne
*/

public class Sonar {
	
	private Scenario scenario;
	private HashSet<Ship> contacts;
	private int designationTicket;
	
	public Sonar(Scenario scenario) {
		this.scenario = scenario;
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
			}
		}
	}

	/**
	 * @return the contacts
	 */
	public HashSet<Ship> getContacts() {
		return contacts;
	}
	
	

}
