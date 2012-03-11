/*
 * @(#) Touchable.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import tween.*;


public class Touchable implements TouchListener, Tweenable {

   // Stores position and rotation info for the touchable object
   protected AffineTransform tform;

   // Current touch events to be processed
   protected TouchFrame tframe;

   // width and height of the object
   protected float width = 0;
   protected float height = 0;

   // minimum/maximum width and height for a resizable widget
   private int minw;
   private int minh;
   private int maxw;
   private int maxh;

   // Last touch location (in object coordinates)
   protected float touchX = 0;
   protected float touchY = 0;

   // Velocity of the object (after being tossed by the user)
   protected float vx = 0;
   protected float vy = 0;
   protected float vr = 0;
   protected float vz = 1;

   // Is the object currently visible?
   protected boolean visible = true;

   // Has the widget been closed by the user?
   protected boolean closed = false;

   // Is the object being dragged on the screen?
   protected boolean dragging = false;

   // Can this object be resized?
   protected boolean resizable = true;

   // Can this object be moved?
   protected boolean movable = true;

   // Can this object be rotated?
   protected boolean rotatable = true;

   // Can this object be tossed?
   protected boolean tossable = true;

   protected TouchListener listener = null;

   // Tween manager
   private TweenManager tweens;

   
   public Touchable() {
      this.tform = new AffineTransform();
      this.tframe = new TouchFrame(this);
      this.minw = 100;
      this.minh = 100;
      this.maxw = 500;
      this.maxh = 500;
      this.listener = this;
      this.tweens = new TweenManager();
   }


   //===============================================================================
   //  Subclasses should override these methods
   //===============================================================================
   public void draw(Graphics2D g) { }

   public void animate() { }
   
   public void touchDown(TouchFrame frame) { }

   public void touchDrag(TouchFrame frame) { }

   public void touchRelease(TouchFrame frame) { }

   public void mousePressed(MouseEvent e) { }
   
   public void mouseReleased(MouseEvent e) { }
   
   public void mouseMoved(MouseEvent e) { }
   
   public void mouseDragged(MouseEvent e) { }
   //===============================================================================


   
   protected void drawWidget(Graphics2D g) {
      if (closed || width <= 0 || height <= 0) return;
      AffineTransform old = g.getTransform();
      g.transform(tform);
      draw(g);
      g.setTransform(old);
   }

   public int getX() {
      return (int) objectToScreenX(0, 0);
   }

   public int getY() {
      return (int) objectToScreenY(0, 0);
   }

   public int getCenterX() {
      return (int) objectToScreenX(width / 2, height / 2);
   }

   public int getCenterY() {
      return (int) objectToScreenY(width / 2, height / 2);
   }

   public void setPosition(float x, float y) {
      translateInWorld(x - getX(), y - getY());
      vx = 0;
      vy = 0;
      vr = 0;
      vz = 1;
   }
   
   public void setCenterPosition(float x, float y) {
      float cx = getCenterX();
      float cy = getCenterY();
      translateInWorld(x - cx, y - cy);
      vx = 0;
      vy = 0;
      vr = 0;
      vz = 1;
   }

   public void toss(float vx, float vy, float vr) {
      this.vx = vx;
      this.vy = vy;
      this.vr = vr;
      this.vz = 1;
   }

   public int getWidth() {
      return (int) this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return (int) this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getMinWidth() {
      return this.minw;
   }

   public void setMinWidth(int minwidth) {
      this.minw = minwidth;
   }

   public int getMinHeight() {
      return this.minh;
   }

   public void setMinHeight(int minheight) {
      this.minh = minheight;
   }

   public int getMaxWidth() {
      return this.maxw;
   }

   public void setMaxWidth(int maxwidth) {
      this.maxw = maxwidth;
   }

   public int getMaxHeight() {
      return this.maxh;
   }

   public void setMaxHeight(int maxheight) {
      this.maxh = maxheight;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean closed) {
      this.closed = closed;
   }

   public void setResizable(boolean b) {
      this.resizable = b;
   }

   public boolean isResizable() {
      return this.resizable;
   }

   public void setMovable(boolean b) {
      this.movable = b;
   }

   public boolean isMovable() {
      return this.movable;
   }

   public void setRotatable(boolean b) {
      this.rotatable = b;
   }

   public boolean isRotatable() {
      return this.rotatable;
   }

   public void setTossable(boolean b) {
      this.tossable = b;
   }

   public boolean isTossable() {
      return this.tossable;
   }

   public TouchListener getListener() {
      return this.listener;
   }

   public void setListener(TouchListener touchlistener) {
      this.listener = touchlistener;
   }

   public void addTween(Tween tween) {
      this.tweens.add(tween);
   }

   public void removeTween(String property) {
      this.tweens.remove(property);
   }

   public void startTween(String property, Tween tween) { }
   
   public void endTween(String property, Tween tween) {  }
   
   public void setTweenValue(String property, Tween tween) { }

   

   /******************************************************************/
   /* TOUCH FUNCTIONS */
   /******************************************************************/
   public TouchFrame getTouchFrame() {
      return this.tframe;
   }

   public boolean isTouched() {
      return !tframe.isEmpty();
   }

   public void startTouchFrame() {
      tframe.startTouchFrame();
   }

   public void addTouchEvent(TouchEvent e) {
      tframe.addTouchEvent(e);
   }

   public void endTouchFrame() {
      tframe.endTouchFrame();

      tweens.animate();

      //------------------------------------------------
      // Touch down
      //------------------------------------------------
      if (tframe.isTouchDown()) {
         this.vx = 0;
         this.vy = 0;
         this.vr = 0;
         this.vz = 1;
         this.dragging = movable && tframe.isAllFingers();
         this.touchX = tframe.getLocalX();
         this.touchY = tframe.getLocalY();
         if (listener != null) listener.touchDown(tframe);
      }

      //------------------------------------------------
      // Touch drag
      //------------------------------------------------
      else if (tframe.isTouchDrag()) {
         if (dragging) {
            if (tframe.isPinchEvent()) {
               pinch(tframe);
            } else if (tframe.isDragEvent()) {
               rnt(tframe);
            }
         }
         this.touchX = tframe.getLocalX();
         this.touchY = tframe.getLocalY();
         if (listener != null) listener.touchDrag(tframe);
      }

      //------------------------------------------------
      // Touch release
      //------------------------------------------------
      else if (tframe.isTouchRelease()) {
         if (listener != null) listener.touchRelease(tframe);
         this.dragging = false;
         this.touchX = 0;
         this.touchY = 0;
      }

      //------------------------------------------------
      // Inertia
      //------------------------------------------------
      if (tframe.isEmpty()) {
         // descrease velocity by coefficient of friction
         vx *= 0.85f;
         vy *= 0.85f;
         vr *= 0.5f;
         if (Math.abs(vz - 1) > 0.01) {
            vz = (float)Math.pow(vz, 0.5);
         } else {
            vz = 1;
         }
         if (Math.abs(vx) < 0.01) vx = 0;
         if (Math.abs(vy) < 0.01) vy = 0;
         if (Math.abs(vr) < 0.01) vr = 0;
         
         if (rotatable) {
            tform.rotate(vr, width / 2, height / 2);
         }
         if (tossable) {
            translateInWorld((int)vx, (int)vy);
         }
         if (resizable) {
            scale(vz, tframe.getLocalX(), tframe.getLocalY());
         }
      }
   }


   protected void pinch(TouchFrame frame) {
      float lx = frame.getLocalX();
      float ly = frame.getLocalY();
      
      if (movable) {
         translateInWorld(frame.getDeltaX(), frame.getDeltaY());
         this.vx = tframe.getDeltaX();
         this.vy = tframe.getDeltaY();
      }
      if (resizable) {
         this.vz = frame.getDiagonal() / frame.getLastDiagonal();
         scale(vz, lx, ly);
      }
      if (rotatable) {
         this.tform.rotate(frame.getDeltaR(), lx, ly);
         this.vr = (float)Math.min(tframe.getDeltaR(), Math.PI / 6);
      }
   }
   

/**
 * Rotate N' Translate (RNT)
 * Kruger, Carpendale, Scott, and Tang. CHI 2005
 */
   protected void rnt(TouchFrame frame) {
      float cx = getWidth() * 0.5f;
      float cy = getHeight() * 0.5f;
      float tx1 = frame.getLocalX();
      float ty1 = frame.getLocalY();
      float tx0 = frame.getLastLocalX();
      float ty0 = frame.getLastLocalY();
      float r = (float)Math.sqrt((tx0 - cx) * (tx0 - cx) + (ty0 - cy) * (ty0 - cy));
      float radius = Math.max(cx, cy) * 0.65f;

      if (rotatable && r > radius) {
         double a0 = Math.atan2(cx - tx0, cy - ty0);
         double a1 = Math.atan2(cx - tx1, cy - ty1);
         double da = (a0 - a1);
         if (da > Math.PI) {
            da -= Math.PI * 2;
         } else if (da < -Math.PI) {
            da += Math.PI * 2;
         }
         this.tform.rotate(da * 0.3, tx1, ty1);
         this.vr = (float)da;
      } else {
         this.vr = 0;
      }
      if (movable) {
         translateInWorld(frame.getDeltaX(), frame.getDeltaY());
         this.vx = tframe.getDeltaX();
         this.vy = tframe.getDeltaY();
      }
   }


   protected void scale(float factor, float cx, float cy) {
      float neww = width * factor;
      float newh = height * factor;
         
      if (neww <= maxw && neww >= minw && newh <= maxh && newh >= minh) {
         this.tform.translate(cx - cx * factor, cy - cy * factor);
         this.width = neww;
         this.height = newh;
      }
   }  


   /**
    * Translate object (dx and dy are in world coordinates)
    */
   public void translateInWorld(float dx, float dy) {
      float tx = getX();
      float ty = getY();
      double ldx = screenToObjectX(tx + dx, ty + dy);
      double ldy = screenToObjectY(tx + dx, ty + dy);
      this.tform.translate(ldx, ldy);
   }

   public void translateInObject(float dx, float dy) {
      this.tform.translate(dx, dy);
   }

   public void rotate(double dr) {
      tform.rotate(dr, width / 2, height / 2);
   }

   public void resetTransform() {
      this.tform = new AffineTransform();
   }
   
   public boolean containsTouch(TouchEvent e) {
      float wx = screenToObjectX(e.getX(), e.getY());
      float wy = screenToObjectY(e.getX(), e.getY());
      return (visible && wx >= 0 && wx <= width && wy >= 0 && wy <= height);
   }

   
   /******************************************************************/
   /* SCREEN / OBJECT TRANSFORMS */
   /******************************************************************/
   double[] temp = new double[2];

   public float screenToObjectX(float wx, float wy) {
      try {
         temp[0] = wx;
         temp[1] = wy;
         tform.inverseTransform(temp, 0, temp, 0, 1);
         return (float) temp[0];
      } catch (NoninvertibleTransformException ntx) {
         return 0;
      }
   }

   public float screenToObjectY(float wx, float wy) {
      try {
         temp[0] = wx;
         temp[1] = wy;
         tform.inverseTransform(temp, 0, temp, 0, 1);
         return (float) temp[1];
      } catch (NoninvertibleTransformException ntx) {
         return 0;
      }
   }

   public float objectToScreenX(float wx, float wy) {
      temp[0] = wx;
      temp[1] = wy;
      tform.transform(temp, 0, temp, 0, 1);
      return (float) temp[0];
   }

   public float objectToScreenY(float wx, float wy) {
      temp[0] = wx;
      temp[1] = wy;
      tform.transform(temp, 0, temp, 0, 1);
      return (float) temp[1];
   }

   public Shape objectToScreen(Shape s) {
      return tform.createTransformedShape(s);
   }
}
