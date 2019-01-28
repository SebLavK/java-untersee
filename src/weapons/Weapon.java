package weapons;

import commons.Vessel;

/**
*@author Sebas Lavigne
*/

public abstract class Weapon {
	
	protected Vessel target;
	protected Vessel owner;
	protected boolean ready;
	protected boolean reloading;
	protected double reloadTime;
	protected double reloadStage;
	
	public void tick() {
		
	}
	
	public abstract void fire();
	
	public void reload() {
		
	}

	/**
	 * @return the target
	 */
	public Vessel getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Vessel target) {
		this.target = target;
	}
	
	
	

}
