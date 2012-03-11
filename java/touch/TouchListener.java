/*
 * @(#) TouchListener.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.awt.event.MouseEvent;


public interface TouchListener {

	public void touchDown(TouchFrame frame);

	public void touchDrag(TouchFrame frame);

	public void touchRelease(TouchFrame frame);

	public boolean containsTouch(TouchEvent touch);

   public void mousePressed(MouseEvent e);
   
   public void mouseReleased(MouseEvent e);
   
   public void mouseMoved(MouseEvent e);
   
   public void mouseDragged(MouseEvent e);

}
