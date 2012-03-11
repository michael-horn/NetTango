/*
 * @(#) TouchEvent.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import org.xml.sax.Attributes;


public class TouchEvent {

   //-------------------------------------------------
   // Top left corner of the surface display
   //-------------------------------------------------
   public static int LEFT = 0;
   public static int TOP = 0;

	public static final int TOUCH_UNKNOWN = 0;
	public static final int TOUCH_FINGER = 1;
	public static final int TOUCH_BLOB = 2;
	public static final int TOUCH_TAG = 3;

	/** Touch ID # (persistent across frames) */
	private int id;

	/** Center of the contact point */
	private float x;
	private float y;

	/** Orientation of the contact point */
	private float o;

	/** Upper left corner of the contact bounding box */
	private float rx;
	private float ry;

	/** Size of the contact bounding box */
	private float rw;
	private float rh;

	/** Value of a fiducial tag (-1 if not a fiducial) */
	private int tagID;

	/** Type of contact ... see constants above */
	private int ttype;

	/** Visual object that this contact is touching */
	private Touchable target;

	/** TouchEvent with same ID from previous frame */
	private TouchEvent prev;

	/** Time of first contact in ms */
	private long time;

	public TouchEvent() {
		this.id = -1;
		this.x = 0;
		this.y = 0;
		this.o = 0;
		this.rx = 0;
		this.ry = 0;
		this.rw = 0;
		this.rh = 0;
		this.tagID = -1;
		this.ttype = TOUCH_UNKNOWN;
		this.target = null;
		this.prev = null;
		this.time = System.currentTimeMillis();
	}

	public TouchEvent(MouseEvent e) {
		this.id = 0;
		this.x = e.getX();
		this.y = e.getY();
		this.o = (float)Math.PI * -0.5f;
		this.rx = x;
		this.ry = y;
		this.rw = 1;
		this.rh = 1;
		this.tagID = -1;
		this.ttype = TOUCH_FINGER;
		this.target = null;
		this.prev = null;
		this.time = System.currentTimeMillis();
	}

	public TouchEvent(Attributes attr) {
		this.id = toInt(attr.getValue("id"));
		this.x = toFloat(attr.getValue("x")) - LEFT;
		this.y = toFloat(attr.getValue("y")) - TOP;
		this.o = toFloat(attr.getValue("orientation"));
		this.rx = toFloat(attr.getValue("boundsX")) - LEFT;
		this.ry = toFloat(attr.getValue("boundsY")) - TOP;
		this.rw = toFloat(attr.getValue("boundsWidth"));
		this.rh = toFloat(attr.getValue("boundsHeight"));
		this.tagID = toInt(attr.getValue("tagID"));
		this.target = null;
		this.prev = null;
		this.time = System.currentTimeMillis();

		// -----------------------------------------------------------
		// Set contact type (finger | blob | tag)
		// -----------------------------------------------------------
		if ("finger".equalsIgnoreCase(attr.getValue("type"))) {
			this.ttype = TOUCH_FINGER;
		} else if ("tag".equalsIgnoreCase(attr.getValue("type"))) {
			this.ttype = TOUCH_TAG;
		} else if ("blob".equalsIgnoreCase(attr.getValue("type"))) {
			this.ttype = TOUCH_BLOB;
		} else {
			this.ttype = TOUCH_BLOB;
		}
		// -----------------------------------------------------------
	}

	public TouchEvent(TouchEvent copy) {
		this.id = copy.id;
		this.x = copy.x;
		this.y = copy.y;
		this.o = copy.o;
		this.rx = copy.rx;
		this.ry = copy.ry;
		this.rw = copy.rw;
		this.rh = copy.rh;
		this.tagID = copy.tagID;
		this.target = copy.target;
		this.prev = copy.prev;
		this.time = copy.time;
		this.ttype = copy.ttype;
	}

	public int getTouchID() {
		return this.id;
	}

	protected void setTouchID(int id) {
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	/**
	 * Returns center of contact in the target's coordinate space.
	 */
	public float getLocalX() {
		if (target == null) {
			return x;
		} else {
			return target.screenToObjectX(x, y);
		}
	}

	/**
	 * Returns center of contact in target's coordinate space.
	 */
	public float getLocalY() {
		if (target == null) {
			return y;
		} else {
			return target.screenToObjectY(x, y);
		}
	}

	public float getOrientation() {
		return this.o + (float)Math.PI / 2;
	}

	public float getRightAngleOrientation() {
		float theta = getOrientation();
		float pi = (float)Math.PI;
		if (theta > 2 * pi) theta -= 2 * pi;
		if (theta >= 0 && theta < pi * 0.25) {
			return 0;
		} else if (theta >= pi * 0.25 && theta < pi * 0.75) {
			return pi * 0.5f;
		} else if (theta >= pi * 0.75 && theta < pi * 1.25) {
			return pi;
		} else if (theta >= pi * 1.25 && theta < pi * 1.75) {
			return pi * 1.5f;
		} else {
			return pi * 2;
		}
	}

	/**
	 * Upper left corner of contact's boundingn box.
	 */
	public float getBoundsX() {
		return this.rx;
	}

	/**
	 * Upper left corner of contact's boundingn box.
	 */
	public float getBoundsY() {
		return this.ry;
	}

	/**
	 * Size of contact's bounding box.
	 */
	public float getWidth() {
		return this.rw;
	}

	/**
	 * Size of contact's bounding box.
	 */
	public float getHeight() {
		return this.rh;
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Float(rx, ry, rw, rh);
	}

	public int getTouchType() {
		return this.ttype;
	}

	public Touchable getTarget() {
		return this.target;
	}

	public void setTarget(Touchable t) {
		this.target = t;
	}

	public boolean hasTarget() {
		return this.target != null;
	}

	public TouchEvent getPrevious() {
		return this.prev;
	}

	public void setPrevious(TouchEvent t) {
		this.prev = t;
	}

	public boolean hasPrevious() {
		return this.prev != null;
	}

	public long getTouchTime() {
		return this.time;
	}

	public void setTouchTime(long touchtime) {
		this.time = touchtime;
	}

	public long getTouchDuration() {
		return System.currentTimeMillis() - time;
	}

	public boolean isFinger() {
		return this.ttype == TOUCH_FINGER;
	}

	public boolean isBlob() {
		return this.ttype == TOUCH_BLOB;
	}

	public boolean isTag() {
		return this.ttype == TOUCH_TAG;
	}

	public boolean isTouchDrag() {
		return this.prev != null;
	}

	public boolean isTouchDown() {
		return this.prev == null;
	}

	public int getTagID() {
		return this.tagID;
	}

	private float toFloat(String v) {
		try {
			return Float.parseFloat(v);
		} catch (NumberFormatException nfx) {
			return 0;
		}
	}

	private int toInt(String v) {
		try {
			return Integer.parseInt(v);
		} catch (NumberFormatException nfx) {
			return 0;
		}
	}
}
