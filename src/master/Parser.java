package master;

import submarine.Submarine;

import java.util.HashMap;

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

	private static ExecutiveOfficer xo;
	private static String[] sentence;
	private static Order<?> order;

	/* Parser maps */
	private static HashMap<String, Runnable> mainMap;
	private static HashMap<String, Runnable> aheadMap;
	private static HashMap<String, Runnable> backMap;
	private static HashMap<String, Runnable> periscopeMap;
	private static HashMap<String, Runnable> emergencyMap;

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
	public static Order getOrder(String command) {
		sentence = command.split(" ");
		try {
			parseCommand();
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
		return order;
	}

	public static void setXo(ExecutiveOfficer xo) {
		Parser.xo = xo;
	}

	public static void initializeMaps() {
		mainMap = new HashMap<>();
			mainMap.put("speed", Parser::parseSpeed);
			mainMap.put("heading", Parser::parseHeading);
			mainMap.put("ahead", Parser::parseAhead);
			mainMap.put("all", Parser::parseAll);
			mainMap.put("back", Parser::parseBack);
			mainMap.put("depth", Parser::parseDepth);
			mainMap.put("surface", Parser::parseSurface);
			mainMap.put("periscope", Parser::parsePeriscope);
			mainMap.put("emergency", Parser::parseEmergency);
			mainMap.put("target", Parser::parseTarget);
			mainMap.put("launch", Parser::parseLaunch);

		aheadMap = new HashMap<>();
			aheadMap.put("flank", Parser::aheadFlank);
			aheadMap.put("full", Parser::aheadFull);
			aheadMap.put("standard", Parser::aheadStandard);
			aheadMap.put("2/3", Parser::ahead23);
			aheadMap.put("1/3", Parser::ahead13);

		backMap = new HashMap<>();
			backMap.put("1/3", Parser::back13);
			backMap.put("2/3", Parser::back23);
			backMap.put("full", Parser::backFull);
			backMap.put("emergency", Parser::backEmerg);

		periscopeMap = new HashMap<>();
			periscopeMap.put("depth", Parser::periscopeDepth);
			//TODO
	//		periscopeMap.put("up", this::periscopeDepth);
	//		periscopeMap.put("down", this::periscopeDepth);

		emergencyMap = new HashMap<>();
			emergencyMap.put("dive", Parser::crashDive);
			emergencyMap.put("blow", Parser::blowBallast);
			//TODO

	}

	/**
	 * Evaluates the first word in a sentence
	 */
	public static void parseCommand() {
		mainMap.get(sentence[0]).run();
	}
	
	public static void parseSpeed() {
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
	
	public static void parseHeading() {
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
	
	public static void parseAhead() {
		aheadMap.get(sentence[1]).run();
	}
	
	public static void parseAll() {
		if (sentence[1].equals("stop")) {
			allStop();
		}
	}
	
	public static void parseBack() {
		backMap.get(sentence[1]).run();
	}
	
	public static void parseDepth() {
		double newDepth = Double.parseDouble(sentence[1]);
		order = new Order<Double>(xo::makeDepth,
				newDepth,
				"Diving: Make my depth " + (int) newDepth + " feet. Dive aye."
				);
	}
	
	public static void parseSurface() {
		if (sentence[1].equals("boat")) {
			order = new Order<Double>(xo::makeDepth,
					Submarine.SURFACE_DEPTH,
					"Diving: Surface the boat. Dive aye."
					);
		}
	}
	
	public static void parsePeriscope() {
		periscopeMap.get(sentence[1]).run();
	}
	
	public static void parseEmergency() {
		emergencyMap.get(sentence[1]).run();
	}
	
	public static void parseTarget() {
		order = new Order<String>(xo::target, sentence[1], "XO:     Targeting "+sentence[1]);
	}
	
	public static void parseLaunch() {
		if (sentence[1].equals("tube")) {
			int tubeNum = Integer.parseInt(sentence[2]);
			if (tubeNum > 0 && tubeNum <= 4) {
				order = new Order<Integer>(
						xo::launchTube,
						tubeNum,
						"WEP O:  Launch tube "+sentence[2]+". Aye sir."
						);
			}
		}
	}
	
	public static void aheadFlank() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_FLANK,
				"Helm:   Engine ahead flank. Helm aye."
				);
	}
	
	public static void aheadFull() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_FULL,
				"Helm:   Engine ahead full. Helm aye."
				);
	}
	
	public static void aheadStandard() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD,
				"Helm:   Engine ahead standard, Helm aye."
				);
	}
	
	public static void ahead23() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD * 2 / 3,
				"Helm:   Engine ahead two thirds. Helm, aye."
				);
	}
	
	public static void ahead13() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD / 3,
				"Helm:   Engine ahead one third. Helm, aye."
				);
	}
	
	public static void allStop() {
		order = new Order<Double>(
				xo::makeSpeed,
				0.0,
				"Helm:   All engines stop. Helm, aye."
				);
	}
	
	public static void back13() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL / 3,
				"Helm:   Engine back one third. Helm, aye."
				);
	}
	
	public static void back23() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL * 2 / 3,
				"Helm:   Engine back two thirds. Helm, aye."
				);
	}
	
	public static void backFull() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL,
				"Helm:   Engine back full. Helm, aye."
				);
	}
	
	public static void backEmerg() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_EMERG,
				"Helm:   Engine back emergency. Helm, aye."
				);
	}
	
	public static void periscopeDepth() {
		if (sentence[1].equals("boat")) {
			order = new Order<Double>(xo::makeDepth,
					Submarine.PERISCOPE_DEPTH,
					"Diving: Go to periscope depth. Dive aye."
					);
		}
	}

	public static void crashDive() {
		Double[] settings = {Submarine.SPEED_FLANK, null, Submarine.TEST_DEPTH};
		order = new Order<Double[]>(xo::makeNav, settings, "Diving: Crash dive! Engine full ahead!");
	}
	
	public static void blowBallast() {
		Double[] settings = {Submarine.SPEED_FLANK, null, Submarine.SURFACE_DEPTH};
		order = new Order<Double[]>(xo::makeNav, settings, "Diving: Blow ballast! Engine full ahead!");
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
