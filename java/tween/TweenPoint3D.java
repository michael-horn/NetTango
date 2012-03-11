/*
 * @(#) TweenPoint3D.java
 */
package tween;

public class TweenPoint3D extends Tween<Point3D> {

   public TweenPoint3D() {
      super();
   }

   public Point3D getValue() {
      float time = getY();
      if (time < 0) time = 0;
      if (time > 1) time = 1;

      double x0 = getStart().x;
      double y0 = getStart().y;
      double z0 = getStart().z;
      double x1 = getEnd().x;
      double y1 = getEnd().y;
      double z1 = getEnd().z;

      Point3D p = new Point3D();
      p.x = (float)(x0 + (x1 - x0) * time);
      p.y = (float)(y0 + (y1 - y0) * time);
      p.z = (float)(z0 + (z1 - z0) * time);
      
      return p;
   }
}
