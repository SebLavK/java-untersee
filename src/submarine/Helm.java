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
			if (Math.abs(sub.getSpeed() - sub.getMySpeed()) < 0.01) {
				speedReached = true;
				ExecutiveOfficer.log("Helm:   We have reached "
						+ String.format("%d", (int) sub.getMySpeed()) + " knots");
			}
		} else {
			speedReached = Math.abs(sub.getSpeed() - sub.getMySpeed()) < 0.01;
		}
		
		if (!headingReached) {
			if (Math.abs(sub.getHeading() - sub.getMyHeading()) < 0.01) {
				headingReached = true;
				ExecutiveOfficer.log("Helm:   Steady on course "
				+ Magnitudes.radiansToHumanDegrees(sub.getMyHeading()) + "º");
			}
		} else {
			headingReached = Math.abs(sub.getHeading() - sub.getMyHeading()) < 0.01;
		}
		
		if (!depthReached) {
			if (Math.abs(sub.getDepth() - sub.getMyDepth()) < 0.01) {
				depthReached = true;
				ExecutiveOfficer.log("Diving: We are at depth "
						+ Magnitudes.feetToHuman(sub.getMyDepth()) + " feet");
			}
		} else {
			depthReached = Math.abs(sub.getDepth() - sub.getMyDepth()) < 0.01;
		}
	}

}
