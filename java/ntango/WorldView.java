/*
 * @(#) WorldView.java
 */
package ntango;

import tween.*;
import touch.*;

import java.util.List;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.event.*;
import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;


public class WorldView extends Touchable {

   protected Model model;
   protected static int MARGIN = 25;
   
   public WorldView(Model model) {
      this.model = model;
      
      setWidth(540 + MARGIN * 2);
      setHeight(540 + MARGIN * 2);
      setMaxWidth(3000);
      setMaxHeight(3000);
      setMinWidth(250);
      setMinHeight(250);
      setVisible(true);
      setMovable(true);
      setResizable(true);
      setRotatable(false);
   }

   public void draw(Graphics2D g) {
      int w = getWidth();
      int h = getHeight();
      g.setColor(new Color(0x33ffffff, true));
      g.fillRect(0, 0, w, h);
      
      if (!model.isLoaded()) {
         g.setFont(Palette.FONT_BODY);
         g.setColor(Color.WHITE);
         String s = "Drop model card here to start...";
         int fw = g.getFontMetrics().stringWidth(s);
         g.drawString(s, w/2 - fw/2, h/2);
      }
      
      g.setColor(Color.WHITE);
      g.setStroke(Palette.STROKE1);
      g.drawRect(0, 0, w, h);

      w -= MARGIN * 2;
      h -= MARGIN * 2;
      int x = MARGIN;
      int y = MARGIN;
      
      g.setColor(new Color(0x55ffffff, true));
      g.fillRect(x, y, w, h);

      float psize = (float)w / model.getWorldWidth();
      Shapes.PATCH_SIZE = psize;
      
      /*
      float x = 0;
      float y = 0;

      float[] dist = { 0.0f, 1.0f };
      Color[] colors = { new Color(0x66ffffff, true), new Color(0x00ffffff, true) };

      RadialGradientPaint paint = new RadialGradientPaint(
         touchX, touchY, psize * 6, dist, colors);
      g.setPaint(paint);
      
      for (int i = model.getMinPX(); i <= model.getMaxPX(); i++) {
         g.drawLine((int)x, (int)y, (int)x, (int)y + h);
         x += psize;
      }

      x = 0;
      y = 0;
      for (int i = model.getMinPY(); i <= model.getMaxPY(); i++) {
         g.drawLine((int)x, (int)y, (int)x + w, (int)y);
         y += psize;
      }
      */

      SimFrame frame = model.getCurrentFrame();
      if (frame != null) {
         Shape clip = g.getClip();
         g.clipRect(x, y, w, h);
         AffineTransform save = g.getTransform();
         g.translate(x + w/2, y + h/2);
         g.scale(1, -1);

         for (Patch patch : frame.getPatches()) {
            patch.draw(g, psize);
         }
         
         for (Turtle turtle : frame.getTurtles()) {
            turtle.draw(g, psize);
         }


         for (Integer id : model.getWatchList()) {
            Turtle t = frame.getTurtle(id);
            if (t != null) {
               t.drawWatch(g, psize, model.getWatchColor(id));
            }
         }
         
         g.setTransform(save);
         g.setClip(clip);
      }

      g.setColor(Color.WHITE);
      g.setStroke(Palette.STROKE1);
      g.drawRect(x, y, w, h);
   }

   public Turtle getTurtleAt(float tx, float ty) {
      int w = getWidth() - MARGIN * 2;
      int h = getHeight() - MARGIN * 2;
      float psize = (float)w / model.getWorldWidth();

      tx = (tx - MARGIN - w/2) / psize;
      ty = (ty - MARGIN - h/2) / -psize;
      SimFrame frame = model.getCurrentFrame();
      if (frame != null) {
         for (Turtle turtle : frame.getTurtles()) {
            if (turtle.containsPoint(tx, ty)) {
               return turtle;
            }
         }
      }
      return null;
   }
   
   public float getModelX(float tx) {
      int w = getWidth() - MARGIN * 2;
      float psize = (float)w / model.getWorldWidth();
      return (tx - MARGIN - w/2) / psize;
   }
   
   
   public float getModelY(float ty) {
      int w = getWidth() - MARGIN * 2;
      int h = getHeight() - MARGIN * 2;
      float psize = (float)w / model.getWorldWidth();
      return (ty - MARGIN - h/2) / -psize;
   }

   public void animate() {
   }

   public void startTween(String property, Tween tween) { }
   public void endTween(String property, Tween tween) { }
   public void setTweenValue(String property, Tween tween) { }
   

   public void onDown() {
      model.doTouchDown(getModelX(touchX), getModelY(touchY));
      /*
     Turtle t = getTurtleAt(touchX, touchY);
     if (t != null) {
        model.toggleWatch(t);
     }
     */
   }
   public void onRelease() { }
   public void onDrag() { }
   public void onHover() { }

   
   public void touchDown(TouchFrame frame) {
      if (frame.isSingleFinger()) {
         onDown();
      }
   }

   public void touchDrag(TouchFrame frame) {
      onDrag();
      for (TouchEvent te : frame.getTouchEvents()) {
         if (te.isTag()) {
            Main.instance.loadModel(te);
         }
      }
   }

   public void touchRelease(TouchFrame frame) {
      onRelease();
   }

   int mouseX;
   int mouseY;
   
   public void mousePressed(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
      onDown();
   }
   
   public void mouseReleased(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
      onRelease();
   }
   
   public void mouseMoved(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
      onHover();
   }
   
   public void mouseDragged(MouseEvent e) {
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
      onDrag();
      translateInWorld(e.getX() - mouseX, e.getY() - mouseY);
      this.mouseX = e.getX();
      this.mouseY = e.getY();
   }
}