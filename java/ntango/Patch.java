/*
 * @(#) Patch.java
 */
package ntango;

import java.awt.Color;
import java.awt.Graphics2D;

public class Patch {

   protected Color color;
   protected int x;
   protected int y;

   
   public Patch(org.nlogo.api.Patch p) {
      this.color = org.nlogo.api.Color.getColor(p.pcolor());
      this.x = p.pxcor();
      this.y = p.pycor();
   }

   public Color getColor() {
      return this.color;
   }

   public void draw(Graphics2D g, float psize) {
      g.setColor(color);
      int px = (int)(x * psize - psize/2);
      int py = (int)(y * psize - psize/2);
      int pw = (int)psize;
      g.fillRect(px-1, py-1, pw+1, pw+1);
   }
}

