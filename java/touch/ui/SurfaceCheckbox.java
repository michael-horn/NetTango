/*
 * @(#) SurfaceCheckbox.java
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
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseEvent;


public class SurfaceCheckbox extends SurfaceButton {

   private boolean checked;
   
   
   public SurfaceCheckbox(String text) {
      super(text);
      this.checked = false;
      setWidth(25);
      setHeight(25);
      setBackground(new Color(0x44000000, true));
      this.foreground = new Color(0x99ffffff, true);
   }

   
   public boolean isChecked() {
      return this.checked;
   }
   
   public void setChecked(boolean checked) {
      this.checked = checked;
   }


   public void draw(Graphics2D g) {
      if (!visible) return;
      
      int w = getWidth();
      int h = getHeight();
      RoundRectangle2D button = new RoundRectangle2D.Float(
         0, 0, w, h, h/2, h/2);

      g.setColor(background);
      g.fill(button);
      GradientPaint paint;

      int y1 = down? -h : h/2;
      int y2 = h;

      paint = new GradientPaint(
         0, y1, new Color(0x00ffffff, true),
         0, y2, new Color(0xaaffffff, true));
      g.setPaint(isEnabled()? paint : background);
      //g.setColor(background);
      g.fill(button);
      
      int fh = 14;
      int fx = w + 10;
      int fy = h/2 + fh/2;
      g.setPaint(new GradientPaint(
                    0, 0, Color.LIGHT_GRAY,
                    0, h, Color.DARK_GRAY));
      g.setStroke(new BasicStroke(1.5f));
      g.setColor(isEnabled() ? Color.LIGHT_GRAY : background);
      g.draw(button);
      g.setFont(font);
      g.setColor(foreground);
      g.drawString(text, fx, fy);

      if (checked) {
         GeneralPath check = new GeneralPath();
         check.moveTo(w/3 - 2, h/3);
         check.lineTo(w/2, h - h/3);
         check.lineTo(w, 0);
         g.setStroke(new BasicStroke(4f));
         g.setColor(foreground);
         g.draw(check);
         g.setStroke(new BasicStroke(1));
      }
   }

   public void touchRelease(TouchFrame frame) {
      if (down && isEnabled()) {
         this.checked = !this.checked;
         buttonReleased();
      }
      this.down = false;
   }

   public void mouseReleased(MouseEvent e) {
      if (down && isEnabled()) {
         this.checked = !this.checked;
         buttonReleased();
      }
      this.down = false;
   }
   
}