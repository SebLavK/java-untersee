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
	public static final long FRAME_PERIOD = 16;
	public static final long FPS = 60;
	public static final long FRAME_NANO = 16666666;
	/** The time spent in frame in seconds */
	public static final double TICK_TIME = 0.016666666;
	
	private static int tickCount;
	private static long gameStartTime;

	private Clock() {}
	
	public static void tick() {
		tickCount++;
	}
	
	
	
	/**
	 * @return the tickCount
	 */
	public static int getTickCount() {
		return tickCount;
	}

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
		tickCount = 0;
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
