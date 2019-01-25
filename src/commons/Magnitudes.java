package commons;

/**
*@author Sebas Lavigne
*/

public class Magnitudes {
	
	/**
	 * In feet/pixel based on the size of the submarine sprite and the length of a real submarine
	 */
	public static final double FEET_PER_PIXEL = 2.485915493;
	
	/**
	 * ft/s / kn
	 */
	public static final double FEET_SECOND_PER_KN = 1.68781;
	
	/**
	 * Pixels per second based on the speed of a expressed in knots
	 */
	public static final double PIXEL_SECOND_PER_KN = 0.678949065144;
	
	/**
	 * @param rad the angle to convert
	 * @return an angle expressed in degrees with leading zeros e.g. 001º
	 */
	public static String radiansToHumanDegrees(double rad) {
		return String.format("%03d", (int) Math.round(Math.toDegrees(rad)));
	}
}
