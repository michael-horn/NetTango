/*
 * @(#) TweenPoint2D.java
 */
package tween;

import java.awt.geom.Point2D;

public class TweenPoint2D extends Tween<Point2D> {


   public TweenPoint2D() {
      super();
   }

   public Point2D getValue() {
      float time = getY();
      if (time < 0) time = 0;
      if (time > 1) time = 1;

      double x0 = getStart().getX();
      double y0 = getStart().getY();
      double x1 = getEnd().getX();
      double y1 = getEnd().getY();

      x0 += (x1 - x0) * time;
      y0 += (y1 - y0) * time;

      return new Point2D.Double(x0, y0);
   }
}
