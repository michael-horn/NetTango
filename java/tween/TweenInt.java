/*
 * @(#) TweenInt.java
 */
package tween;

public class TweenInt extends Tween<Integer> {

   public TweenInt() {
      super();
   }

   public Integer getValue() {
      float time = getTime();
      int a = getStart();
      int b = getEnd();
      double y = getY();
      return (int) ( y * (b - a) + a );
   }
}
