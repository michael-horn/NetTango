/*
 * @(#) Pen.java
 */
package ntango;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

public class Pen {

   protected String name;
   protected Color color;
   protected BasicStroke stroke;
   protected GeneralPath path;
   protected boolean empty;
   protected float interval;
   protected float x;
   protected float startX;
   protected Rectangle2D bounds;

   public Pen(String name) {
      this.name = name;
      this.color = Color.RED;
      this.stroke = new BasicStroke(1f);
      this.path = new GeneralPath();
      this.empty = true;
      this.interval = 1.0f;
      this.x = 0;
      this.startX = 0;
      this.bounds = new Rectangle2D.Float(0, 0, 0, 0);
   }

   public Pen(org.nlogo.plot.PlotPen pen) {
      this(pen.name());
      this.interval = (float)pen.interval();
      this.color = org.nlogo.api.Color.getColor(
         org.nlogo.api.Color.argbToColor(pen.color()));
      /*
      java.lang.reflect.Method [] methods = pen.getClass().getMethods();
      for (int i=0; i<methods.length; i++) {
         System.out.println(methods[i]);
      }
      */
   }

   public void draw(Graphics2D g, float scaleX, float scaleY) {
      g.setStroke(stroke);
      g.setColor(color);
      AffineTransform tform = AffineTransform.getScaleInstance(scaleX, scaleY);
      Shape s = tform.createTransformedShape(path);
      g.draw(s);
   }

   public void clear() {
      this.path.reset();
      this.empty = true;
      this.x = startX;
   }

   public void plot(float y) {
      if (empty) {
         path.moveTo(x, y);
         empty = false;
      } else {
         path.lineTo(x, y);
      }
      x += interval;
      bounds = path.getBounds2D();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public float getInterval() {
      return this.interval;
   }

   public void setInterval(float interval) {
      this.interval = interval;
   }

   public void setStartX(float startx) {
      this.startX = startx;
      this.x = startx;
   }

   public float getMinX() {
      return (float)bounds.getMinX();
   }

   public float getMaxX() {
      return (float)bounds.getMaxX();
   }

   public float getMinY() {
      return (float)bounds.getMinY();
   }

   public float getMaxY() {
      return (float)bounds.getMaxY();
   }
}

   