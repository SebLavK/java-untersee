package master;

import commons.gameObject.Order;
import commons.gameObject.Verbose;
import submarine.Submarine;

import java.util.HashMap;

/**
 * @author Sebas Lavigne
 */

/**
 * The Parser evaluates the commands received by the player and dispatches them
 * to the FirstOfficer so they can be executed.
 * <p>
 * Parsing works as follows: a map is generated with <key, value> as <command, action>
 * An action is obtained via the key and executed. This action may generate an Order object
 * or may parse more commands.
 * <p>
 * Every time a command has to be parsed a new Parser object is instantiated. It will
 * generate maps as needed and execute their actions.
 * //TODO maybe change this in the future
 */
public class Parser {

	public static final String HEADER_HELM = "header.helm";
	public static final String HEADER_DIVE = "header.dive";
	public static final String ACKNOWLEDGEMENT_DIVE = "acknowledgement.dive";
	public static final String ACKNOWLEDGEMENT_HELM = "acknowledgement.helm";
	private static ExecutiveOfficer xo;
	private static String[] sentence;
	private static Order<?> order;

	/* Parser maps */
	private static HashMap<String, Runnable> mainMap;
	private static HashMap<String, Runnable> aheadMap;
	private static HashMap<String, Runnable> backMap;
	private static HashMap<String, Runnable> periscopeMap;
	private static HashMap<String, Runnable> emergencyMap;

	static {
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

	private Parser() {
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
		order = new Order<>(
				xo::makeSpeed,
				newSpeed,
				new Verbose(HEADER_HELM,
						"ack.helm.set.speed",
						new String[]{sentence[1]},
						"acknowledgement.maneuvering")
		);
	}

	public static void parseHeading() {
		//TODO: change it to "come [right/left] to course XXX
		double newHeading = Double.parseDouble(sentence[1]);
		newHeading %= 360;
		if (newHeading == 360) {
			newHeading = 0;
		}
		order = new Order<>(
				xo::makeHeading,
				newHeading,
				new Verbose(HEADER_HELM,
						"ack.helm.set.heading",
						new String[]{Double.toString(newHeading)},
						"acknowledgement.maneuvering")
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
		order = new Order<>(xo::makeDepth,
				newDepth,
				new Verbose(HEADER_DIVE,
						"ack.dive.set.depth",
						new String[]{Integer.toString((int) newDepth)},
						ACKNOWLEDGEMENT_DIVE)
		);
	}

	public static void parseSurface() {
		if (sentence[1].equals("boat")) {
			order = new Order<>(xo::makeDepth,
					Submarine.SURFACE_DEPTH,
					new Verbose(HEADER_DIVE,
							"ack.dive.depth.surface",
							null,
							ACKNOWLEDGEMENT_DIVE)
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
		order = new Order<>(xo::target, sentence[1],
				new Verbose("header.xo",
						"ack.xo.set.target",
						new String[]{sentence[1]})
		);
	}

	public static void parseLaunch() {
		if (sentence[1].equals("tube")) {
			int tubeNum = Integer.parseInt(sentence[2]);
			if (tubeNum > 0 && tubeNum <= 4) {
				order = new Order<>(
						xo::launchTube,
						tubeNum,
						new Verbose("header.weapons",
								"ack.weapons.launch",
								new String[]{sentence[2]},
								"acknowledgement.weapons")
				);
			}
		}
	}

	public static void aheadFlank() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_FLANK,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.flank",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void aheadFull() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_FULL,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.full",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void aheadStandard() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.standard",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void ahead23() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD * 2 / 3,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.2.3",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void ahead13() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_STANDARD / 3,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.1.3",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void allStop() {
		order = new Order<>(
				xo::makeSpeed,
				0.0,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.stop",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void back13() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL / 3,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.reverse.1.3",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void back23() {
		order = new Order<Double>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL * 2 / 3,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.reverse.2.3",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void backFull() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_FULL,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.reverse.full",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void backEmerg() {
		order = new Order<>(
				xo::makeSpeed,
				Submarine.SPEED_BACK_EMERG,
				new Verbose(HEADER_HELM,
						"ack.helm.speed.reverse.emergency",
						null,
						ACKNOWLEDGEMENT_HELM)
		);
	}

	public static void periscopeDepth() {
		if (sentence[1].equals("boat")) {
			order = new Order<>(xo::makeDepth,
					Submarine.PERISCOPE_DEPTH,
					new Verbose(HEADER_DIVE,
							"ack.dive.depth.periscope",
							null,
							ACKNOWLEDGEMENT_DIVE)
			);
		}
	}

	public static void crashDive() {
		Double[] settings = {Submarine.SPEED_FLANK, null, Submarine.TEST_DEPTH};
		order = new Order<>(xo::makeNav, settings,
				new Verbose(HEADER_DIVE,
						"ack.dive.depth.crash")
		);
	}

	public static void blowBallast() {
		Double[] settings = {Submarine.SPEED_FLANK, null, Submarine.SURFACE_DEPTH};
		order = new Order<>(xo::makeNav, settings,
				new Verbose(HEADER_DIVE,
						"ack.dive.depth.blow.ballas")
		);
	}

}
