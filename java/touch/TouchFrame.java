/*
 * @(#) TouchFrame.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.util.List;
import java.awt.geom.Rectangle2D;


/**
 * Represents the set of touch contact points that apply to a single Touchable
 * object
 * 
 * @author Michael Horn
 * @version $Revision: 1.4 $, $Date: 2008-12-02 13:46:50 $
 */
public class TouchFrame {

	/** Center of the frame's bounding box */
	protected float x = 0;
	protected float y = 0;

	/** Previous center of the bounding box */
	protected float lx = 0;
	protected float ly = 0;

	protected List<TouchEvent> curr;
	protected List<TouchEvent> prev;

	/** Deltas between the current frame and the previous frame in object space */
	protected float deltaX = 0;
	protected float deltaY = 0;

	/** The Touchable object that this frame belongs to */
	protected Touchable target;

	public TouchFrame(Touchable target) {
		this.curr = new java.util.ArrayList<TouchEvent>();
		this.prev = new java.util.ArrayList<TouchEvent>();
		this.target = target;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getLastX() {
		return this.lx;
	}

	public float getLastY() {
		return this.ly;
	}

	public float getLocalX() {
		return target.screenToObjectX(x, y);
	}

	public float getLocalY() {
		return target.screenToObjectY(x, y);
	}

	public float getLastLocalX() {
		return target.screenToObjectX(lx, ly);
	}

	public float getLastLocalY() {
		return target.screenToObjectY(lx, ly);
	}

	public float getDiagonal() {
		Rectangle2D rect = getLocalBounds();
		double w = rect.getWidth();
		double h = rect.getHeight();
		return (float)Math.sqrt(w * w + h * h);
	}

	public float getLastDiagonal() {
		Rectangle2D rect = getLastLocalBounds();
		double w = rect.getWidth();
		double h = rect.getHeight();
		return (float)Math.sqrt(w * w + h * h);
	}

	public float getDeltaX() {
		return this.deltaX;
	}

	public float getDeltaY() {
		return this.deltaY;
	}

	public float getDeltaR() {
		if (isPinchEvent()) {
			double cx0 = getLastX();
			double cy0 = getLastY();
			double cx1 = getX();
			double cy1 = getY();
			double a = 0;
			
			for (TouchEvent e : curr) {
				double x0 = e.getPrevious().getX();
				double x1 = e.getX();
				double y0 = e.getPrevious().getY();
				double y1 = e.getY();
				double r0 = Math.atan2(x0 - cx0, y0 - cy0);
				double r1 = Math.atan2(x1 - cx1, y1 - cy1);
				double da = (r0 - r1);
				if (da > Math.PI) {
					da -= Math.PI * 2;
				} else if (da < -Math.PI) {
					da += Math.PI * 2;
				}
				a += da;
			}
			return (float)(a / curr.size());
		}
		return 0;
	}

	public Rectangle2D getLocalBounds() {
		return getLocalBounds(curr);
	}

	public Rectangle2D getLastLocalBounds() {
		return getLocalBounds(prev);
	}

	private Rectangle2D getLocalBounds(List<TouchEvent> frame) {
		float x0 = getX();
		float x1 = x0;
		float y0 = getY();
		float y1 = y0;
		float cx, cy;
		
		for (TouchEvent e : frame) {
			cx = e.getBoundsX();
			cy = e.getBoundsY();
			x0 = Math.min(x0, cx);
			y0 = Math.min(y0, cy);
			x1 = Math.max(x1, cx + e.getWidth());
			y1 = Math.max(y1, cy + e.getHeight());
		}
		cx = target.screenToObjectX(x0, y0);
		cy = target.screenToObjectY(x0, y0);
		x0 = cx; y0 = cy;
		cx = target.screenToObjectX(x1, y1);
		cy = target.screenToObjectY(x1, y1);
		x1 = cx; y1 = cy;
		return new Rectangle2D.Float(x0, y0, x1 - x0, y1 - y0);
	}

	public boolean isEmpty() {
		return curr.isEmpty();
	}

	public boolean isTouchDown() {
		return (curr.size() > 0 && prev.size() == 0);
	}

	public boolean isTouchDrag() {
		return (curr.size() > 0 && prev.size() > 0);
	}

	public boolean isTouchRelease() {
		return (curr.size() == 0 && prev.size() > 0);
	}

	public boolean isPinchEvent() {
		if (isMultipleFingers(curr) && isMultipleFingers(prev)) {
			for (TouchEvent e : curr) {
				if (!e.hasPrevious()) return false;
			}
			return (curr.size() == prev.size());
		}
		return false;
	}

	public boolean isDragEvent() {
		if (isAllFingers(curr) && isAllFingers(prev)) {
			for (TouchEvent e : curr) {
				if (!e.hasPrevious()) return false;
			}
			return (curr.size() == prev.size());
		}
		return false;
	}

	public boolean isSingleFinger() {
		return isSingleFinger(curr);
	}

	public boolean isMultipleFingers() {
		return isMultipleFingers(curr);
	}

	private boolean isMultipleFingers(List<TouchEvent> frame) {
		if (frame.size() < 2) return false;
		for (TouchEvent e : frame) {
			if (!e.isFinger()) return false;
		}
		return true;
	}

	private boolean isSingleFinger(List<TouchEvent> frame) {
		if (frame.size() != 1) {
			return false;
		} else {
			return frame.get(0).isFinger();
		}
	}

	public boolean isAllFingers() {
		return isAllFingers(curr);
	}

	private boolean isAllFingers(List<TouchEvent> frame) {
		if (frame.isEmpty())	return false;
		for (TouchEvent e : frame) {
			if (!e.isFinger())
				return false;
		}
		return true;
	}

	public List<TouchEvent> getTouchEvents() {
		return this.curr;
	}

	public TouchEvent getTouchEvent(int index) {
		return this.curr.get(index);
	}

	public void addTouchEvent(TouchEvent e) {
		this.curr.add(e);
	}

	public void startTouchFrame() {
		this.prev = curr;
		this.curr = new java.util.ArrayList<TouchEvent>();
		this.lx = x;
		this.ly = y;
	}

	public void endTouchFrame() {
		if (!curr.isEmpty()) {
			this.x = 0;
			this.y = 0;
			for (TouchEvent e : curr) {
				x += e.getX();
				y += e.getY();
			}
			x /= curr.size();
			y /= curr.size();
		}

		if (isTouchDown()) {
			this.deltaX = 0;
			this.deltaY = 0;
		} else if (isTouchDrag()) {
			this.deltaX = x - lx;
			this.deltaY = y - ly;
		}
	}
}
