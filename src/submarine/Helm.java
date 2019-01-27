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
	private boolean depthReached;
	
	public Helm(Submarine sub) {
		super();
		this.sub = sub;
		speedReached = true;
		headingReached = true;
		depthReached = true;
	}

	public void tick() {
		if (!speedReached) {
			if (sub.getSpeed() == sub.getMySpeed()) {
				speedReached = true;
				ExecutiveOfficer.log("Helm:   We have reached "
						+ String.format("%d", (int) sub.getMySpeed()) + " knots");
			}
		} else {
			speedReached = sub.getSpeed() == sub.getMySpeed();
		}
		
		if (!headingReached) {
			if (sub.getHeading() == sub.getMyHeading()){
				headingReached = true;
				ExecutiveOfficer.log("Helm:   Steady on course "
				+ Magnitudes.radiansToHumanDegrees(sub.getMyHeading()) + "º");
			}
		} else {
			headingReached = sub.getHeading() == sub.getMyHeading();
		}
		
		if (!depthReached) {
			if (sub.getDepth() == sub.getMyDepth()) {
				depthReached = true;
				ExecutiveOfficer.log("Diving: We are at depth "
						+ Magnitudes.feetToHuman(sub.getMyDepth()) + " feet");
			}
		} else {
			depthReached = sub.getDepth() == sub.getMyDepth();
		}
	}

}
