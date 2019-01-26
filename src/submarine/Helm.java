package submarine;

import commons.Magnitudes;
import master.ExecutiveOfficer;

/**
*@author Sebas Lavigne
*/

public class Helm {
	
	private Submarine sub;
	private boolean speedReached;
	private boolean headingReached;
	
	public Helm(Submarine sub) {
		super();
		this.sub = sub;
		speedReached = true;
		headingReached = true;
	}

	public void tick() {
		if (!speedReached) {
			if (sub.getSpeed() == sub.getMySpeed()) {
				speedReached = true;
				ExecutiveOfficer.log("Helm:\tWe have reached "
						+ String.format("%d", (int) sub.getMySpeed()) + " knots");
			}
		} else {
			speedReached = sub.getSpeed() == sub.getMySpeed();
		}
		
		if (!headingReached) {
			if (sub.getHeading() == sub.getMyHeading()){
				headingReached = true;
				ExecutiveOfficer.log("Helm:\tWe are now heading "
				+ Magnitudes.radiansToHumanDegrees(sub.getMyHeading()) + "º");
			}
		} else {
			headingReached = sub.getHeading() == sub.getMyHeading();
		}
	}

}
