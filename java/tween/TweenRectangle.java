/*
 * @(#) TweenRectangle.java
 * 
 * Michael Horn
 * AmphibiaWeb Visualizer
 * Northwestern University
 */
package tween;

import java.awt.geom.Rectangle2D;

public class TweenRectangle extends Tween<Rectangle2D> {

   public TweenRectangle() {
      super();
   }

   public Rectangle2D getValue() {
      float time = getY();
      if (time < 0) time = 0;
      if (time > 1) time = 1;

      double x0 = getStart().getX();
      double y0 = getStart().getY();
      double w0 = getStart().getWidth();
      double h0 = getStart().getHeight();

      double x1 = getEnd().getX();
      double y1 = getEnd().getY();
      double w1 = getEnd().getWidth();
      double h1 = getEnd().getHeight();

      x0 += (x1 - x0) * time;
      y0 += (y1 - y0) * time;
      w0 += (w1 - w0) * time;
      h0 += (h1 - h0) * time;

      return new Rectangle2D.Double(x0, y0, w0, h0);
   }
}

