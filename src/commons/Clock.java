package commons;

/**
*@author Sebas Lavigne
*/

/**
 * Clock is a utility class used for time related calculations in the game
 * There can be no instantiation of Clock objects
 */
public final class Clock {
	
	/** The time between frames in milliseconds */
	public final static long FRAME_PERIOD = 16;
	/** The time spent in frame in seconds */
	public final static double TICK_TIME = 0.016;
	
	private static long gameStartTime;

	private Clock() {}
	
	/**
	 * @return the gameStartTime
	 */
	public static long getGameStartTime() {
		return gameStartTime;
	}

	/**
	 * Sets the current time as the game starting time
	 */
	public static void setGameStartTime() {
		if (gameStartTime == 0) {
			gameStartTime = System.nanoTime();
		} else {
			System.out.println("gameStartTime is already set");
		}
	}

	/**
	 * @return the time passed since the beginning of the game
	 */
	public static long timeSinceStart() {
		return System.nanoTime() - gameStartTime;
	}
	
	/**
	 * @return the current system time
	 */
	public static long getCurrentTime() {
		return System.nanoTime();
	}
	
	public static long timeSince(long then) {
		return System.nanoTime() - then;
	}
	
}
