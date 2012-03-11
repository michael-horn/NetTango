/*
 * @(#) TweenColor.java
 */
package tween;

import java.awt.Color;

public class TweenColor extends Tween<Color> {


   public TweenColor() {
      super();
   }

   public Color getValue() {
      float time = getY();
      if (time < 0) time = 0;
      if (time > 1) time = 1;

      int r1 = getStart().getRed();
      int g1 = getStart().getGreen();
      int b1 = getStart().getBlue();
      int a1 = getStart().getAlpha();
      
      int r2 = getEnd().getRed();
      int g2 = getEnd().getGreen();
      int b2 = getEnd().getBlue();
      int a2 = getEnd().getAlpha();

      r1 += ((r2 - r1) * time);
      g1 += ((g2 - g1) * time);
      b1 += ((b2 - b1) * time);
      a1 += ((a2 - a1) * time);

      return new Color(r1, g1, b1, a1);
   }
}
