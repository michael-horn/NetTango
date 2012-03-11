/*
 * @(#) TweenFloat.java
 */
package tween;

public class TweenFloat extends Tween<Float> {

   public TweenFloat() {
      super();
   }

   public Float getValue() {
      float time = getTime();
      float a = getStart();
      float b = getEnd();
      double y = getY();
      return (float) ( y * (b - a) + a );
   }
}
