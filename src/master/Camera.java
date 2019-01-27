package master;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import commons.Magnitudes;
import commons.Vessel;
import submarine.Submarine;

/**
*@author Sebas Lavigne
*/

/**
 *
 */
public class Camera extends Vessel implements MouseWheelListener, MouseListener {

	private boolean followSub;
	private boolean trackMouse;
	private Point mouseOriginPoint;
	private double zoom;
	private Submarine sub;
	

	public Camera(Submarine sub) {
		super();
		this.sub = sub;
		followSub = true;
		trackMouse = false;
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
		if (trackMouse && !followSub) {
			trackMouse();
		}
	}

	public void switchFollowSub() {
		followSub = !followSub;
		if (followSub) {
			this.position = sub.getPosition();
		} else {
			//New Point2D object, since this.position get referenced to the sub's
			this.position = new Point2D.Double(position.getX(), position.getY());
		}
	}
	
	
	
	/**
	 * @param trackMouse the trackMouse to set
	 */
	public void setTrackMouse(boolean trackMouse) {
		this.trackMouse = trackMouse;
		if (trackMouse) {
			mouseOriginPoint = MouseInfo.getPointerInfo().getLocation();
		}
	}

	public void trackMouse() {
		Point mouseDragPoint = MouseInfo.getPointerInfo().getLocation();
		Point2D dragAmount = new Point2D.Double(mouseOriginPoint.getX() - mouseDragPoint.getX(),
				mouseOriginPoint.getY() - mouseDragPoint.getY());
		this.position = new Point2D.Double(
				position.getX() + dragAmount.getX() / Magnitudes.FEET_PER_PIXEL * zoom,
				position.getY() - dragAmount.getY() / Magnitudes.FEET_PER_PIXEL * zoom);
		
		mouseOriginPoint = mouseDragPoint;
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			switchFollowSub();
		}
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			setTrackMouse(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			setTrackMouse(false);
		}
	}
	
	
}
