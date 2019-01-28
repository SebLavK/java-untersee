package weapons;


/**
*@author Sebas Lavigne
*/

public class Torpedo extends Projectile {
	
	private double blastRadius;
	
	
	
	@Override
	public void tick() {
		super.tick();
	}

	public void explode() {
		if (distanceTo(target) < blastRadius) {
			target.setDestroyed(true);
			this.destroyed = true;
		}
	}
	
	public void reacquireTarget() {
		
	}
	
	

}
