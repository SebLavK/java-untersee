package master;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import commons.Vessel;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

public class Camera extends Vessel implements MouseWheelListener{

	private boolean followSub;
	private double zoom;
	private Submarine sub;
	

	public Camera(Submarine sub) {
		super();
		this.sub = sub;
		followSub = true;
		zoom = 2;
	}
	
	public void zoomIn() {
		zoom -= 0.1;
		if (zoom < 0.5) {
			zoom = 0.5;
		}
	}
	
	public void zoomOut() {
		zoom += 0.1;
	}
	
	/* (non-Javadoc)
	 * @see commons.Vessel#tick()
	 */
	@Override
	public void tick() {
//		super.tick();
		if (followSub) {
			this.position = sub.getPosition();
		}
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

	/**
	 * @return the zoom
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * @param zoom the zoom to set
	 */
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() > 0) {
			zoomOut();
		} else if (e.getWheelRotation() < 0) {
			zoomIn();
		}
	}
	
	
}
