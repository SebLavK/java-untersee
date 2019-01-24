package master;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 * The Parser evaluates the commands received by the player and dispatches them
 * to the FirstOfficer so they can be executed.
 */
public class Parser {

	public static final String[] NAV_COMMANDS = { "speed", "heading", "ahead", "all", "back" };
	//TODO define method reference as a constant as well, linked to these commands

	private ExecutiveOfficer xo;
	
	private HashMap<String, Function<String[], Double>> explicitMap;
	private HashMap<String, Function<String[], String>> implicitMap;
	private HashMap<String, Supplier<Double>> navOrderMap;

	public Parser(ExecutiveOfficer xo) {
		this.xo = xo;
		createOrderMap();
	}
	
	public void createOrderMap() {
		explicitMap = new HashMap<>();
		implicitMap = new HashMap<>();
		navOrderMap = new HashMap<>();
		
		explicitMap.put("speed", Parser::parseSecond);
		
		implicitMap.put("ahead", Parser::getSecond);
		
		navOrderMap.put("flank", Parser::aheadFlank);
		
	}
	
	public static String getSecond(String[] sentence) {
		return sentence[1];
	}
	
	public static double parseSecond(String[] second) {
		return Double.parseDouble(second[1]);
	}
	
	public static double aheadFull() {
		return Submarine.SPEED_FULL;
	}
	
	public static double aheadFlank() {
		return Submarine.SPEED_FLANK;
	}
	
	

	/**
	 * Evaluates the command given by the player into an order executable by the XO
	 * @param fullCommand
	 * @return an Order
	 */
	public Order parseCommand(String fullCommand) {
		String[] sentence = fullCommand.toLowerCase().split(" ");
		
		if (implicitMap.containsKey(sentence[0])) {
			if(navOrderMap.containsKey(sentence[1])) {
				System.out.println("POW");
				return new Order(xo::makeSpeed, navOrderMap.get(sentence[1]).get(), "");
			}
		}
		return null;
		
		
		
//		if (Arrays.toString(NAV_COMMANDS).contains(sentence[0])) {
//			return parseNavigationCommand(sentence);
//		} else {
//			return new Order(xo::unkownCommand, null, null);
//		}
	}

	public Order parseNavigationCommand(String[] sentence) {
		String r = ""; // r for response
		Consumer<Order> c = null; 
		Double v = null; // v for value
		switch (sentence[0]) {
		case "speed":
			c = xo::makeSpeed;
			v = Double.parseDouble(sentence[1]);
			r = "Make my speed "+sentence[1]+" knots";
			return new Order(c,  v,  r);
		case "heading":
			c = xo::makeHeading;
			v = Double.parseDouble(sentence[1]);
			r = "Make my heading "+sentence[1]+"º"; 
			break;
		case "ahead": case "back":
			return parseAheadOrBackCommand(sentence);
		case "all":
			if (sentence[1].equals("stop")) {
				c = xo::makeSpeed;
				v = 0.0;
				r = "All engines stop";
			}
			break;
		default:
			c = xo::unkownCommand;
			break;
		}
		return new Order(c,  v,  r);
	}
	
	public Order parseAheadOrBackCommand(String[] sentence) {
		String r = ""; // r for verbose response
		Consumer<Order> c = xo::makeSpeed; 
		Double v = null; // v for value
		
		if (sentence[0].equals("ahead")) {
			switch (sentence[1]) {
			case "flank":
				v = Submarine.SPEED_FLANK;
				r = "Ahead flank";
				break;
			case "full":
				v = Submarine.SPEED_FULL;
				r = "Ahead full";
				break;
			case "standard":
				v = Submarine.SPEED_STANDARD;
				r = "Ahead standard";
				break;
			case "2/3":
				v = Submarine.SPEED_STANDARD * 2 / 3;
				r = "Ahead two thirds";
				break;
			case "1/3":
				v = Submarine.SPEED_STANDARD / 3;
				r = "Ahead one third";
				break;
			default:
				c = xo::unkownCommand;
				break;
				
			}
		} else if (sentence[0].equals("back")) {
			switch (sentence[1]) {
			case "emergency":
				v = Submarine.SPEED_BACK_EMERG;
				r = "Back emergency";
				System.out.println("POW");
				break;
			case "full":
				v = Submarine.SPEED_BACK_FULL;
				r = "Back full";
				break;
			case "2/3":
				v = Submarine.SPEED_BACK_FULL * 2 / 3;
				r = "Back two thirds";
				break;
			case "1/3":
				v = Submarine.SPEED_BACK_FULL / 3;
				r = "Back one third";
				break;
			default:
				c = xo::unkownCommand;
				break;
			}
		} else {
			c = xo::unkownCommand;
		}
		
		return new Order(c, v, r);
	}
	
}
