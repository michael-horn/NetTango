/*
 * @(#) TouchDelegator.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;


public interface TouchDelegator {

	public Touchable findTouchTarget(TouchEvent e);

	/**
	 * The delegator must relay these messages to all Touchables
	 */
	public void startTouchFrame(int touch_count);

	public void endTouchFrame();

	public void closeApplication();

}
