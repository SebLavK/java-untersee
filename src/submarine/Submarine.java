package submarine;

import commons.Vessel;

/**
*@author Sebas Lavigne
*/

public class Submarine extends Vessel {
	
	public static final double SPEED_FLANK = 35;
	public static final double SPEED_FULL = 20;
	public static final double SPEED_STANDARD = 15;
	public static final double SPEED_BACK_EMERG = -16;
	public static final double SPEED_BACK_FULL = -12;
	
	private Helm helm;
	
	private double depth;
	private double maxDepth;
	
	public Submarine() {
		super();
		helm = new Helm(this);
	}

	/* (non-Javadoc)
	 * @see commons.Vessel#tick()
	 */
	@Override
	public void tick() {
		super.tick();
		helm.tick();
	}
	
	
	
	

}
