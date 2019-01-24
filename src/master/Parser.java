package master;

import java.util.Arrays;
import java.util.function.Consumer;

import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 * The Parser evaluates the commands received by the player and dispatches them
 * to the FirstOfficer so they can be executed.
 */
public class Parser {

	public static final String[] NAV_COMMANDS = { "speed", "heading", "ahead", "engine", "back" };

	private ExecutiveOfficer xo;

	public Parser(ExecutiveOfficer xo) {
		this.xo = xo;
	}

	public Order parseCommand(String fullCommand) {
		String[] sentence = fullCommand.toLowerCase().split(" ");
		
		if (Arrays.toString(NAV_COMMANDS).contains(sentence[0])) {
			return parseNavigationCommand(sentence);
		} else {
			xo.unkownCommand();
		}
	}

	public Order parseNavigationCommand(String[] sentence) {
		boolean success = true;
		String r = ""; // r for response
		Consumer<Double> c = null; 
		double v = 0; // v for value
		switch (sentence[0]) {
		case "speed":
			c = xo::makeSpeed;
			v = Double.parseDouble(sentence[1]);
			r = "Make my speed "+sentence[1]+" knots"; 
			break;
		case "heading":
			c = xo::makeHeading;
			v = Double.parseDouble(sentence[1]);
			r = "Make my heading "+sentence[1]+"º"; 
			break;
		case "ahead": case "back":
			parseAheadOrBackCommand(sentence);
			break;
		case "engine":
			if (sentence[1].equals("stop")) {
				c = xo::makeSpeed;
				v = 0;
				r = "Engine stop";
			}
			break;
		default:
			success = false;
			break;
		}
		
		if (success) {
			xo.execute(c, v, r);
		} else {
			xo.unkownCommand();
		}
	}
	
	public Order parseAheadOrBackCommand(String[] sentence) {
		boolean success = true;
		String r = ""; // r for verbose response
		Consumer<Double> c = xo::makeSpeed; 
		Double v = 0; // v for value
		
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
				success = false;
				break;
				
			}
		} else if (sentence[1].equals("back")) {
			switch (sentence[1]) {
			case "emergency":
				v = Submarine.SPEED_BACK_EMERG;
				r = "Back emergency";
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
				success = false;
				break;
			}
		} else {
			success = false;
		}
		
		if (success) {
			return new Order(c, v, r);
		} else {
			xo.unkownCommand();
		}
	}
	
}
