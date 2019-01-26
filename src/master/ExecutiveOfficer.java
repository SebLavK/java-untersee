package master;

import java.util.Scanner;

import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 * The first officer acts as an interpreter for the player's commands, giving "orders"
 * to the submarine accordingly.
 * It also notifies the player of certain events ingame.
 * 
 * It will send the player's command to a parser and interpret the order it generates.
 */
public class ExecutiveOfficer implements Runnable {

	private Submarine sub;
	
	public ExecutiveOfficer(Submarine sub) {
		this.sub = sub;
	}
	
	public void initialize() {
//		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Parser parser = new Parser(this, readCommand());
				@SuppressWarnings("unchecked")
				Order<Object> order = parser.getOrder();
//				execute(new Parser(this, readCommand()).getOrder());
				execute(order);
			} catch (NullPointerException e) {
				unkownCommand();
			}
		}
	}
	
	public void sendCommand(String command) {
		try {
			Parser parser = new Parser(this, command);
			@SuppressWarnings("unchecked")
			Order<Object> order = parser.getOrder();
//			execute(new Parser(this, readCommand()).getOrder());
			execute(order);
		} catch (NullPointerException e) {
			unkownCommand();
		}
	}
	
	public String readCommand() {
		return new Scanner(System.in).nextLine();
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Order<Object> order) {
		order.getVerb().accept(order);
		log(order.getVerbose() + ", aye sir.");
	}
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	/**
	 * Changes the set speed of the sub
	 * @param newSpeed an Order with a Double as object
	 */
	public void makeSpeed(Order<Double> newSpeed) {
		sub.setMySpeed((Double) newSpeed.getObject());
	}
	
	/**
	 * Changes the set heading of the sub
	 * @param newHeading an Order with a Double as object
	 */
	public void makeHeading(Order<Double> newHeading) {
		sub.setMyHeading(Math.toRadians((Double) newHeading.getObject()));
	}
	
	/**
	 * To be used when the parser doesn't generate an order, either by failure or
	 * mistype of the player
	 */
	public void unkownCommand() {
		System.out.println("I don't understand that command, sir");
	}

	
}
