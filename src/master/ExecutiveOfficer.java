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
		parser = new Parser(this);
		new Thread(this).start();
	}
	
	public String readCommand() {
		return new Scanner(System.in).nextLine();
	}
	
	public void execute(Consumer<Double> c, double value, String verbose) {
		System.out.println("POW");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.accept(value);
		System.out.println(verbose);
	}
	
	public void makeSpeed(double newSpeed) {
		sub.setMySpeed(newSpeed);
	}
	
	public void makeHeading(double newHeading) {
		sub.setMyHeading(Math.toRadians(newHeading));
	}
	
	public void unkownCommand() {
		System.out.println("I don't understand that command, sir");
	}

	@Override
	public void run() {
		while (true) {
			parser.parseCommand(readCommand());
		}
	}
}
