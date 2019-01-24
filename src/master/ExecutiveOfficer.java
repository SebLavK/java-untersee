package master;

import java.util.Scanner;
import java.util.function.Consumer;

import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 * The first officer acts as an interpreter for the player's commands, giving "orders" to the
 * submarine accordingly. It also notifies the player of certain events ingame.
 */
public class ExecutiveOfficer implements Runnable {

	private Submarine sub;
	private Parser parser;
	
	public ExecutiveOfficer(Submarine sub) {
		this.sub = sub;
	}
	
	public void initialize() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				execute(new Parser(this, readCommand()).getOrder());
			} catch (NullPointerException e) {
				unkownCommand();
			}
		}
	}
	
	public String readCommand() {
		return new Scanner(System.in).nextLine();
	}
	
	public void execute(Order order) {
		order.getVerb().accept(order);
		log(order.getVerbose() + ", aye sir.");
	}
	
	public void log(String msg) {
		System.out.println(msg);
	}
	
	public void makeSpeed(Order newSpeed) {
		sub.setMySpeed((Double) newSpeed.getObject());
	}
	
	public void makeHeading(Order newHeading) {
		sub.setMyHeading(Math.toRadians((Double) newHeading.getObject()));
	}
	
	public void unkownCommand() {
		System.out.println("I don't understand that command, sir");
	}

	
}
