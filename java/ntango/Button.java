/*
 * @(#) Button.java
 */
package ntango;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Button {

   public static final Color LIGHT_GRAY = new Color(0xe0e0e0);
   public static final Color GRAY = new Color(0x999999);
   public static final Color DARK_GRAY = Color.DARK_GRAY;
   public static final BasicStroke STROKE1 = new BasicStroke(1f);

   protected Shape shape;
   protected String label;
   protected String action;
   protected Shape icon = null;
   protected BufferedImage image = null;
   protected boolean enabled = true;
   protected boolean down = false;
   protected boolean visible = true;
   protected boolean checked = false;

   public Button(String action) {
      this.shape = null;
      this.action = action;
      this.label = null;
   }
   
   public Button(Shape shape, String action) {
      this.shape = shape;
      this.action = action;
      this.label = null;
   }
   
   public Button(int x, int y, int w, int h, String action) {
      this(new RoundRectangle2D.Float(x, y, w, h, h/2, h/2), action);
   }

   public void reshape(int x, int y, int w, int h) {
      this.shape = new RoundRectangle2D.Float(x, y, w, h, h/2, h/2);
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getAction() {
      return this.action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public boolean isChecked() {
      return this.checked;
   }
   
   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }
   
   public boolean isDown() {
      return this.down;
   }
   
   public void setDown(boolean down) {
      this.down = down;
   }

   public boolean isEnabled() {
      return this.enabled;
   }
   
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public Shape getShape() {
      return this.shape;
   }
   
   public void setShape(Shape s) {
      this.shape = s;
   }

   public Shape getIcon() {
      return this.icon;
   }

   public void setIcon(Shape icon) {
      this.icon = icon;
   }

   public BufferedImage getImage() {
      return this.image;
   }
   
   public void setImage(BufferedImage image) {
      this.image = image;
   }
   
   public boolean containsTouch(float tx, float ty) {
      return this.shape.contains(tx, ty);
   }

   public void draw(Graphics2D g) {

      if (!visible) return;
      
      Rectangle rect = shape.getBounds();
      int bx = (int)rect.getX();
      int by = (int)rect.getY();
      int bw = (int)rect.getWidth();
      int bh = (int)rect.getHeight();
      
      /*
      if (checked) {
         g.setColor(Color.GRAY);
      }

      g.setPaint(
         new GradientPaint(
            bx, by, LIGHT_GRAY,
            bx, by + bh, GRAY));
      g.fill(shape);
      g.setColor(DARK_GRAY);
      g.setStroke(STROKE1);
      g.draw(shape);
      */
      
      if (image != null) {
         int iw = image.getWidth();
         int ih = image.getHeight();
         g.drawImage(image, bx + bw/2 - iw/2, by + bh/2 - ih/2, null);
      } else if (icon != null) {
         g.setColor(enabled ? Color.BLACK : GRAY);
         g.fill(icon);
      }

      if (down) {
         g.setPaint(new Color(0x99ffffff, true));
         g.fill(shape);
      }
   }
}
