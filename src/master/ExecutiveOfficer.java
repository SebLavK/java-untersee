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

	private Master master;
	private Submarine sub;
	
	public ExecutiveOfficer(Master master, Submarine sub) {
		this.master = master;
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
		log(order.getVerbose());
	}
	
	public static void log(String msg) {
//		System.out.println(msg);
		Master.master.getSidePanel().addToLog(msg);
	}
	
	/**
	 * Changes the set speed of the sub
	 * @param speedOrder an Order with a Double as object
	 */
	public void makeSpeed(Order<Double> speedOrder) {
		double newSpeed = (double) speedOrder.getObject();
		if (newSpeed > Submarine.SPEED_FLANK) {
			newSpeed = Submarine.SPEED_FLANK;
		} else if (newSpeed < Submarine.SPEED_BACK_EMERG) {
			newSpeed = Submarine.SPEED_BACK_EMERG;
		}
		sub.setMySpeed(newSpeed);
	}
	
	/**
	 * Changes the set heading of the sub
	 * @param headingOrder an Order with a Double as object
	 */
	public void makeHeading(Order<Double> headingOrder) {
		double newHeading = (Double) headingOrder.getObject();
		newHeading %= 360;
		if (newHeading == 360) {
			newHeading = 0;
		}
		sub.setMyHeading(Math.toRadians(newHeading));
	}
	
	/**
	 * Changes the set depth of the sub
	 * @param depthOrder an Order with a Double as object
	 */
	public void makeDepth(Order<Double> depthOrder) {
		double newDepth = (Double) depthOrder.getObject();
		if (newDepth < 0) {
			newDepth = 0;
		}
		sub.setMyDepth(newDepth);
	}
	
	/**
	 * To be used when the parser doesn't generate an order, either by failure or
	 * mistype of the player
	 */
	public void unkownCommand() {
//		System.out.println("I don't understand that command, sir");
		master.getSidePanel().addToLog("XO:     I don't understand that command, sir");
	}

	
}
