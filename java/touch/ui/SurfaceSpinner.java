/*
 * @(#) SurfaceSpinner.java
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


public class SurfaceSpinner extends SurfaceWidget {

   protected int max;
   protected int min;
   protected int value;
   
   public SurfaceSpinner(String text) {
      super(text);
      this.max = 20;
      this.min = 0;
      this.value = 10;
      setWidth(50);
      setHeight(105);
      setFont(new java.awt.Font(null, 0, 30));
   }

   public int getMaxValue() {
      return this.max;
   }

   public void setMaxValue(int maxvalue) {
      this.max = maxvalue;
   }

   public int getMinValue() {
      return this.min;
   }

   public void setMinValue(int minvalue) {
      this.min = minvalue;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   private int getButtonHeight() {
      return 30;
   }

   
   public void draw(Graphics2D g) {
      if (!visible) return;
      
      int w = getWidth();
      int h = getHeight();
      RoundRectangle2D button = new RoundRectangle2D.Float(
         0, 0, w, h, 10, 10);

      g.setColor(background);
      g.fill(button);

      int y1 = 0;
      int y2 = getButtonHeight();

      GradientPaint paint = new GradientPaint(
         0, y1, new Color(0xaaffffff, true),
         0, y2, new Color(0x00ffffff, true));
      g.setPaint(paint);
      g.fill(button);

      y1 = h - getButtonHeight();
      y2 = h;

      paint = new GradientPaint(
         0, y1, new Color(0xaaffffff, true),
         0, y2, new Color(0x33ffffff, true));
      g.setPaint(paint);
      g.fill(button);

      g.setColor(Color.GRAY);
      g.drawLine(0, y1, w, y1);
      
      y1 = getButtonHeight();
      g.setColor(Color.GRAY);
      g.drawLine(0, y1, w, y1);


      g.setFont(font);
      g.setColor(foreground);
      drawCenteredString(String.valueOf(value), h - getButtonHeight() - 10, g);

      g.setFont(new java.awt.Font(null, java.awt.Font.BOLD, 25));
      g.setColor(Color.GRAY);
      drawCenteredString("+", getButtonHeight() - 6, g);
      drawCenteredString("-", h - 6, g);
      
      g.setPaint(new GradientPaint(
                    0, 0, Color.LIGHT_GRAY,
                    0, h, Color.DARK_GRAY));
      g.setStroke(new BasicStroke(1.5f));
      g.draw(button);
   }

   protected void drawCenteredString(String s, int fy, Graphics2D g) {
      int fw = g.getFontMetrics().stringWidth(s);
      int fx = getWidth()/2 - fw/2;
      g.drawString(s, fx, fy);
   }

   protected boolean onPlusButton(float tx, float ty) {
      int w = getWidth();
      int h = getHeight();
      return (tx >= 0 && ty >= -5 && tx <= w && ty <= getButtonHeight() + 8);
   }

   protected boolean onMinusButton(float tx, float ty) {
      int w = getWidth();
      int h = getHeight();
      int by = h - getButtonHeight();
      return (tx >= 0 && ty >= by - 8 && tx <= w && ty <= h + 8);
   }      

   
   public void touchDown(TouchFrame frame) {
      float tx = frame.getLocalX();
      float ty = frame.getLocalY();
      if (onPlusButton(tx, ty)) {
         this.value++;
      } else if (onMinusButton(tx, ty)) {
         this.value--;
      }
   }

   public void touchDrag(TouchFrame frame) {
   }

   public void touchRelease(TouchFrame frame) {
   }

   public void mousePressed(MouseEvent e) {
      float tx = screenToObjectX(e.getX(), e.getY());
      float ty = screenToObjectY(e.getX(), e.getY());
      if (onPlusButton(tx, ty)) {
         this.value++;
      } else if (onMinusButton(tx, ty)) {
         this.value--;
      }
   }
   
   public void mouseReleased(MouseEvent e) {
   }

   public void mouseDragged(MouseEvent e) {
   }
}