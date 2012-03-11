/*
 * @(#) SurfaceSlider.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch.ui;

import touch.TouchFrame;
import java.awt.Font;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseEvent;


public class SurfaceSlider extends SurfaceWidget {


   protected int max;
   protected int min;
   protected int value;
   protected boolean down;
   protected String unit;
   protected ButtonListener listener;
   

   public SurfaceSlider(String text) {
      super(text);
      this.max = 30;
      this.min = 0;
      this.value = 10;
      this.down = false;
      this.listener = null;
      this.unit = "";
      setFont(new java.awt.Font(null, 0, 20));
      setBackground(new Color(0x44000000, true));
   }

   public ButtonListener getButtonListener() {
      return this.listener;
   }

   public void setButtonListener(ButtonListener listener) {
      this.listener = listener;
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

   public void setUnit(String unit) {
      this.unit = unit;
   }

   public String getUnit() {
      return this.unit;
   }

   protected boolean onBall(float tx, float ty) {

      float bw = getBallWidth() * 2;
      float bx = getBallX() - bw/2;
      float by = getBallY() - bw/2;

      return (tx >= bx && tx <= bx + bw && ty >= by && ty <= by + bw);
   }
   
   public void draw(Graphics2D g) {
      int w = getWidth();
      int h = getHeight();

      RoundRectangle2D box = new RoundRectangle2D.Float(
         0, 0, w, h, 20, 20);
      g.setColor(background);
      g.fill(box);

      GradientPaint paint;
      
      /*
      GradientPaint paint = new GradientPaint(
         0, 0, Color.DARK_GRAY, 0, h, Color.LIGHT_GRAY);
      g.setPaint(paint);
      */

      int bx = (int)getBallX();
      int by = (int)getBallY();
      int bw = (int)getBallWidth();
      int minX = (int)getBallX(this.min);
      int maxX = (int)getBallX(this.max);

      
      g.setColor(isEnabled() ? Color.GRAY : background);
      g.setStroke(new BasicStroke(1.5f));
      g.drawLine(minX, by, maxX, by);
      g.drawLine(maxX + (int)getMargin(), 0, maxX + (int)getMargin(), h);

      g.setColor(isEnabled() ? Color.LIGHT_GRAY : background);
      g.setStroke(new BasicStroke(1.5f));
      g.draw(box);
      
      // Draw ticks
      //g.drawLine(minX, by - bw/4, minX, by + bw/4);
      //g.drawLine(maxX, by - bw/4, maxX, by + bw/4);

      Ellipse2D ball = new Ellipse2D.Float(
         bx - bw/2, by - bw/2, bw, bw);

      g.setColor(isEnabled() ? Color.GRAY : background);
      g.fill(ball);
      int y1 = by - bw/2;
      int y2 = down? by + bw * 2 : by + bw/2;

      if (isEnabled()) {
         paint = new GradientPaint(
            0, y1, new Color(0xddffffff, true),
            0, y2, new Color(0x00ffffff, true));
         g.setPaint(paint);
         g.fill(ball);

         paint = new GradientPaint(
            0, by - bw/2, Color.LIGHT_GRAY,
            0, by + bw/2, Color.BLACK);
         g.setPaint(paint);
         g.draw(ball);
      }

      g.setColor(Color.LIGHT_GRAY);
      g.setFont(font);
      String s = String.valueOf(value) + unit;
      g.drawString(s, w - getGutter() + 5, h - 17);

      g.setFont(new Font(null, 0, 14));
      g.drawString(getText(), minX, h - 4);
      
   }

   private float getMargin() {
      return getBallWidth() / 2 + 4;
   }

   private float getGutter() {
      return 65;
   }
   
   private float getBallX(float v) {
      float w = getWidth() - getMargin() * 2 - getGutter();
      float range = max - min;
      float scale = (range / w);
      return (v - min) / scale + getMargin();
   }
   
   private float getBallX() {
      return getBallX(this.value);
   }

   private float getBallY() {
      return getHeight() / 2 - 6;
   }
   
   private float getValue(float bx) {
      float w = getWidth() - getMargin() * 2 - getGutter();
      float range = max - min;
      float scale = (range / w);
      return ((bx - getMargin()) * scale) + min;
   }

   private float getBallWidth() {
      return 20;
   }

   private void computeValue(float tx) {
      this.value = (int)getValue(tx);
      if (value < min) value = min;
      if (value > max) value = max;
   }

   public void touchDown(TouchFrame frame) {
      if (onBall(frame.getLocalX(), frame.getLocalY())) {
         this.down = true;
      }
   }

   public void touchDrag(TouchFrame frame) {
      if (down && isEnabled()) {
         computeValue(frame.getLocalX());
      }
   }

   public void touchRelease(TouchFrame frame) {
      if (down && isEnabled()) buttonReleased();
      this.down = false;
   }

   public void mousePressed(MouseEvent e) {
      float tx = screenToObjectX(e.getX(), e.getY());
      float ty = screenToObjectY(e.getX(), e.getY());
      if (onBall(tx, ty)) {
         this.down = true;
      }
   }
   
   public void mouseReleased(MouseEvent e) {
      if (down && isEnabled()) buttonReleased();
      this.down = false;
   }

   public void mouseDragged(MouseEvent e) {
      if (down && isEnabled()) {
         computeValue(screenToObjectX(e.getX(), e.getY()));
      }
   }

   public void buttonReleased() {
      if (listener != null) {
         listener.buttonReleased(this);
      }
   }
}
