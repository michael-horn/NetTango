/*
 * @(#) SurfaceButton.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch.ui;

import touch.TouchFrame;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseEvent;


public class SurfaceButton extends SurfaceWidget {

   protected boolean down;
   protected ButtonListener listener;
   
   
   public SurfaceButton(String text) {
      super(text);
      this.down = false;
      this.listener = null;
   }

   public ButtonListener getButtonListener() {
      return this.listener;
   }

   public void setButtonListener(ButtonListener listener) {
      this.listener = listener;
   }

   public void draw(Graphics2D g) {
      if (!visible) return;
      
      int w = getWidth();
      int h = getHeight();
      RoundRectangle2D button = new RoundRectangle2D.Float(
         0, 0, w, h, 20, 20);

      g.setColor(background);
      g.fill(button);

      int y1 = 0;
      int y2 = down ? h*2 : h/2;

      GradientPaint paint = new GradientPaint(
         0, y1, new Color(0xaaffffff, true),
         0, y2, new Color(0x33ffffff, true));
      g.setPaint(paint);
      g.fill(button);

      g.setFont(font);
      int fw = g.getFontMetrics().stringWidth(text);
      int fh = 14;
      int fx = w/2 - fw/2;
      int fy = h/2 + fh/2;
      if (down) {
         fx++;
         fy++;
      }
      
      g.setColor(foreground);
      g.drawString(text, fx, fy);
      
      g.setPaint(new GradientPaint(
                    0, 0, Color.LIGHT_GRAY,
                    0, h, Color.DARK_GRAY));
      g.setStroke(new BasicStroke(1f));
      g.draw(button);

   }

   public void buttonPressed() {
      if (listener != null) {
         listener.buttonPressed(this);
      }
   }

   public void buttonReleased() {
      if (listener != null) {
         listener.buttonReleased(this);
      }
   }

   protected boolean onButton(float tx, float ty) {
      int w = getWidth();
      int h = getHeight();
      return (tx >= 0 && ty >= -5 && tx <= w && ty <= h + 5);
   }

   
   public void touchDown(TouchFrame frame) {
      this.down = true;
      buttonPressed();
   }

   public void touchDrag(TouchFrame frame) {
      this.down = onButton(frame.getLocalX(), frame.getLocalY());
   }

   public void touchRelease(TouchFrame frame) {
      if (down) buttonReleased();
      this.down = false;
   }

   public void mousePressed(MouseEvent e) {
      this.down = true;
      buttonPressed();
   }
   
   public void mouseReleased(MouseEvent e) {
      if (down) buttonReleased();
      this.down = false;
   }

   public void mouseDragged(MouseEvent e) {
      this.down = onButton(
         screenToObjectX(e.getX(), e.getY()),
         screenToObjectY(e.getX(), e.getY()));
   }
}