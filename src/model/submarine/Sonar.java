package model.submarine;

import commons.Clock;
import commons.Magnitudes;
import commons.gameObject.Verbose;
import model.ExecutiveOfficer;
import model.Scenario;
import model.ships.Ship;
import model.ships.Vessel;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
		scenario.getShips().stream()
				.filter(e -> !contacts.contains(e))
				.forEach(this::addContact);
	}

	private void addContact(Ship ship) {
		designationTicket++;
		ship.setDesignation("S"+designationTicket);
		contacts.add(ship);
		String bearing = Magnitudes.radiansToHumanDegrees(sub.bearingTo(ship));
		ExecutiveOfficer.log(new Verbose("header.sonar",
				"update.sonar.new.contact",
				new String[]{bearing, ship.getDesignation()}));
	}

	public void removeDestroyed() {
		contacts.removeIf(Vessel::isDestroyed);
	}

	public void target(String designation) {
		Optional<Ship> target =
				contacts.stream()
						.filter(e -> e.getDesignation().equalsIgnoreCase(designation))
						.findAny();

		if (target.isPresent()) {
			sub.setTarget(target.get());
		} else {
			ExecutiveOfficer.log(new Verbose("header.sonar",
					"reply.sonar.unknown.contact"));
		}
	}

	/**
	 * @return the contacts
	 */
	public Set<Ship> getContacts() {
		return contacts;
	}
	
	

}
