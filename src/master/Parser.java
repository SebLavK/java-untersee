package master;

import java.util.HashMap;

import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 * The Parser evaluates the commands received by the player and dispatches them
 * to the FirstOfficer so they can be executed.
 * 
 * Parsing works as follows: a map is generated with <key, value> as <command, action>
 * An action is obtained via the key and executed. This action may generate an Order object
 * or may parse more commands.
 * 
 *  Every time a command has to be parsed a new Parser object is instantiated. It will
 *  generate maps as needed and execute their actions.
 *  //TODO maybe change this in the future
 */
public class Parser {

	private ExecutiveOfficer xo;
	private String[] sentence;
	private Order<?> order;
	
	/**
	 * 
	 * @param xo the ExecutiveOfficer that will interpret the Order
	 * @param command the sentence to parse
	 */
	public Parser(ExecutiveOfficer xo, String command) {
		this.xo = xo;
		this.sentence = command.split(" ");
		try {
			parseCommand();
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @return the order
	 */
	@SuppressWarnings("rawtypes")
	public Order getOrder() {
		return order;
	}

	/**
	 * Evaluates the first word in a sentence
	 */
	public void parseCommand() {
		HashMap<String, Runnable> parseCommand = new HashMap<>();
		parseCommand.put("speed", this::parseSpeed);
		parseCommand.put("heading", this::parseHeading);
		parseCommand.put("ahead", this::parseAhead);
		parseCommand.put("all", this::parseAll);
		parseCommand.put("back", this::parseBack);
		parseCommand.put("depth", this::parseDepth);
		
		parseCommand.get(sentence[0]).run();
	}
	
	public void parseSpeed() {
		double newSpeed = Double.parseDouble(sentence[1]);
		if (newSpeed > Submarine.SPEED_FLANK) {
			newSpeed = Submarine.SPEED_FLANK;
		} else if (newSpeed < Submarine.SPEED_BACK_EMERG) {
			newSpeed = Submarine.SPEED_BACK_EMERG;
		}
		order = new Order<Double>(
				xo::makeSpeed,
				newSpeed,
				"Helm:   Make turns for "+sentence[1]+" knots. Maneuvering aye."
				);
	}
	
	public void parseHeading() {
		//TODO: change it to "come [right/left] to course XXX
		double newHeading = Double.parseDouble(sentence[1]);
		newHeading %= 360;
		if (newHeading == 360) {
			newHeading = 0;
		}
		order = new Order<Double>(
				xo::makeHeading,
				newHeading,
				"Helm:   Come to course " + newHeading + "º. Maneuvering aye."
				);
	}
	
	public void parseAhead() {
		HashMap<String, Runnable> parseCommand = new HashMap<>();
		parseCommand.put("flank", this::aheadFlank);
		parseCommand.put("full", this::aheadFull);
		parseCommand.put("standard", this::aheadStandard);
		parseCommand.put("2/3", this::ahead23);
		parseCommand.put("1/3", this::ahead13);
		
		parseCommand.get(sentence[1]).run();
	}
	
	public void aheadFlank() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_FLANK,
				"Helm:   Engine ahead flank. Helm aye."
				);
	}
	
	public void aheadFull() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_FULL,
				"Helm:   Engine ahead full. Helm aye."
				);
	}
	
	public void aheadStandard() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD,
				"Helm:   Engine ahead standard, Helm aye."
				);
	}
	
	public void ahead23() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD * 2 / 3,
				"Helm:   Engine ahead two thirds. Helm, aye."
				);
	}
	
	public void ahead13() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD / 3,
				"Helm:   Engine ahead one third. Helm, aye."
				);
	}
	
	public void parseAll() {
		if (sentence[1].equals("stop")) {
			allStop();
		}
	}
	
	public void allStop() {
		order = new Order<Double>(
				xo::makeSpeed,
				0.0,
				"Helm:   All engines stop. Helm, aye."
				);
	}
	
	public void parseBack() {
		HashMap<String, Runnable> parseCommand = new HashMap<>();
		parseCommand.put("1/3", this::back13);
		parseCommand.put("2/3", this::back23);
		parseCommand.put("full", this::backFull);
		parseCommand.put("emergency", this::backEmerg);
		
		parseCommand.get(sentence[1]).run();
	}
	
	public void back13() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL / 3,
				"Helm:   Engine back one third. Helm, aye."
				);
	}
	
	public void back23() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL * 2 / 3,
				"Helm:   Engine back two thirds. Helm, aye."
				);
	}
	
	public void backFull() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL,
				"Helm:   Engine back full. Helm, aye."
				);
	}
	
	public void backEmerg() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_EMERG,
				"Helm:   Engine back emergency. Helm, aye."
				);
	}

	public void parseDepth() {
		double newDepth = Double.parseDouble(sentence[1]);
		order = new Order<Double>(xo::makeDepth,
				newDepth,
				"Diving: Make my depth " + (int) newDepth + " feet. Dive aye."
				);
	}
//	/**
//	 * Evaluates the command given by the player into an order executable by the XO
//	 * @param fullCommand
//	 * @return an Order
//	 */
//	public Order parseCommand(String fullCommand) {
//		String[] sentence = fullCommand.toLowerCase().split(" ");
//		
//		if (implicitMap.containsKey(sentence[0])) {
//			if(navOrderMap.containsKey(sentence[1])) {
//				System.out.println("POW");
//				return new Order(xo::makeSpeed, navOrderMap.get(sentence[1]).get(), "");
//			}
//		}
//		return null;
//		
//		
//		
//		if (Arrays.toString(NAV_COMMANDS).contains(sentence[0])) {
//			return parseNavigationCommand(sentence);
//		} else {
//			return new Order(xo::unkownCommand, null, null);
//		}
//	}

//	public Order parseNavigationCommand(String[] sentence) {
//		String r = ""; // r for response
//		Consumer<Order> c = null; 
//		Double v = null; // v for value
//		switch (sentence[0]) {
//		case "speed":
//			c = xo::makeSpeed;
//			v = Double.parseDouble(sentence[1]);
//			r = "Make my speed "+sentence[1]+" knots";
//			return new Order(c,  v,  r);
//		case "heading":
//			c = xo::makeHeading;
//			v = Double.parseDouble(sentence[1]);
//			r = "Make my heading "+sentence[1]+"º"; 
//			break;
//		case "ahead": case "back":
//			return parseAheadOrBackCommand(sentence);
//		case "all":
//			if (sentence[1].equals("stop")) {
//				c = xo::makeSpeed;
//				v = 0.0;
//				r = "All engines stop";
//			}
//			break;
//		default:
//			c = xo::unkownCommand;
//			break;
//		}
//		return new Order(c,  v,  r);
//	}
//	
//	public Order parseAheadOrBackCommand(String[] sentence) {
//		String r = ""; // r for verbose response
//		Consumer<Order> c = xo::makeSpeed; 
//		Double v = null; // v for value
//		
//		if (sentence[0].equals("ahead")) {
//			switch (sentence[1]) {
//			case "flank":
//				v = Submarine.SPEED_FLANK;
//				r = "Ahead flank";
//				break;
//			case "full":
//				v = Submarine.SPEED_FULL;
//				r = "Ahead full";
//				break;
//			case "standard":
//				v = Submarine.SPEED_STANDARD;
//				r = "Ahead standard";
//				break;
//			case "2/3":
//				v = Submarine.SPEED_STANDARD * 2 / 3;
//				r = "Ahead two thirds";
//				break;
//			case "1/3":
//				v = Submarine.SPEED_STANDARD / 3;
//				r = "Ahead one third";
//				break;
//			default:
//				c = xo::unkownCommand;
//				break;
//				
//			}
//		} else if (sentence[0].equals("back")) {
//			switch (sentence[1]) {
//			case "emergency":
//				v = Submarine.SPEED_BACK_EMERG;
//				r = "Back emergency";
//				System.out.println("POW");
//				break;
//			case "full":
//				v = Submarine.SPEED_BACK_FULL;
//				r = "Back full";
//				break;
//			case "2/3":
//				v = Submarine.SPEED_BACK_FULL * 2 / 3;
//				r = "Back two thirds";
//				break;
//			case "1/3":
//				v = Submarine.SPEED_BACK_FULL / 3;
//				r = "Back one third";
//				break;
//			default:
//				c = xo::unkownCommand;
//				break;
//			}
//		} else {
//			c = xo::unkownCommand;
//		}
//		
//		return new Order(c, v, r);
//	}
	
}
