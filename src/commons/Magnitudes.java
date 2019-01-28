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
	 * Feet per nautical mile
	 */
	public static final double FEET_PER_NM = 6076.12;
	
	public static final double FEET_PER_YARD = 3;
	
	/**
	 * Feet per kiloyard
	 */
	public static final double FEET_PER_KYD = 3000;
	
	/**
	 * @param rad the angle to convert
	 * @return an angle expressed in degrees with leading zeros e.g. 001º
	 */
	public static String radiansToHumanDegrees(double rad) {
		return String.format("%03d", (int) Math.round(Math.toDegrees(rad)));
	}
	
	/**
	 * 
	 * @param knots the speed to convert
	 * @return the speed expressed as a two digit integer
	 */
	public static String knotsToHuman(double knots) {
		return String.format("%02d", (int) Math.round(knots));
	}
	
	public static String feetToHuman(double feet) {
		return String.format("% 4d", (int)Math.round(feet));
	}
	
	public static String feetToNauticalMilesHuman(double feet) {
		return String.format("%.1f", feet / FEET_PER_NM);
	}
	
	public static String feetToKydHuman(double feet) {
		return String.format("% 2.1f", feet / FEET_PER_KYD);
	}
	
	public static String feetToYardHuman(double feet) {
		return String.format("% 3d", (int) Math.round(feet / FEET_PER_YARD));
	}
}
