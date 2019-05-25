package model.submarine;

import commons.Magnitudes;
import commons.gameObject.Verbose;
import model.ExecutiveOfficer;

/**
 * @author Sebas Lavigne
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
				ExecutiveOfficer.log(new Verbose("header.helm",
						"update.helm.reached.speed",
						new String[]{Integer.toString((int) sub.getMySpeed())})
				);
			}
		} else {
			speedReached = Math.abs(sub.getSpeed() - sub.getMySpeed()) < 0.01;
		}

		if (!headingReached) {
			if (Math.abs(sub.getHeading() - sub.getMyHeading()) < 0.01) {
				headingReached = true;
				ExecutiveOfficer.log(new Verbose("header.helm",
						"update.helm.reached.course",
						new String[]{Magnitudes.radiansToHumanDegrees(sub.getMyHeading())})
				);

			}
		} else {
			headingReached = Math.abs(sub.getHeading() - sub.getMyHeading()) < 0.01;
		}

		if (!depthReached) {
			if (Math.abs(sub.getDepth() - sub.getMyDepth()) < 0.01) {
				depthReached = true;
				ExecutiveOfficer.log(new Verbose("header.dive",
						"update.dive.reached.depth",
						new String[]{Magnitudes.feetToHuman(sub.getMyDepth())})
				);
			}
		} else {
			depthReached = Math.abs(sub.getDepth() - sub.getMyDepth()) < 0.01;
		}
	}

}
