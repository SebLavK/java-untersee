package weapons;

/**
*@author Sebas Lavigne
*/

public class TorpedoTube extends Weapon {
	

	@Override
	public void fire() {
		Torpedo torpedo = new Torpedo();
		torpedo.setTarget(target);
		owner.getScenario().getProjectiles().add(torpedo);
	}

	
	
	
}
