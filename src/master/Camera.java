package master;

import commons.Vessel;

/**
*@author Sebas Lavigne
*/

public class Camera extends Vessel {

	private boolean followSub;
	
	

	public Camera() {
		super();
		followSub = false;
	}

	/**
	 * @return the followSub
	 */
	public boolean isFollowSub() {
		return followSub;
	}

	/**
	 * @param followSub the followSub to set
	 */
	public void setFollowSub(boolean followSub) {
		this.followSub = followSub;
	}
	
	
}
