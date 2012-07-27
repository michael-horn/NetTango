/*
 * @(#) Toolbar.java
 */
package ntango;

import tween.*;
import touch.*;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.List;


public class Toolbar extends Touchable {

   protected List<Button> buttons;
   protected Button target = null;
   protected Model model;
   protected int pstate = 0;
   protected Button playButton;
   protected Button pauseButton;
   protected Button scrubButton;
   protected Button fullButton;
   protected Button partButton;
   protected int scrubMax;
   protected boolean fullscreen = false;

   
   public Toolbar(Model model) {
      setWidth(440);
      setHeight(70);
      setVisible(true);
      setMovable(false);
      setResizable(false);
      setRotatable(false);

      this.model = model;
      this.buttons = new java.util.ArrayList<Button>();
      this.scrubMax = model.getStream().getCapacity();

      int w = getWidth();
      int h = getHeight();
      int bw = 40;
      int bh = 30;
      int bx = 0;
      int by = 8;

      Button button;

      button = new Button(w/2 - bw/2 + 3, by, bw, bh, "play");
      button.setImage(Palette.createImage("/images/play.png"));
      buttons.add(button);
      button.setEnabled(pstate == 0);
      button.setVisible(pstate == 0);
      playButton = button;
      
      button = new Button(w/2 - bw/2 + 3, by, bw, bh, "pause");
      button.setImage(Palette.createImage("/images/pause.png"));
      button.setVisible(pstate != 0);
      button.setEnabled(pstate != 0);
      buttons.add(button);
      pauseButton = button;
      bx += bw;

      button = new Button(w/2 + bx - bw/2, by, bw, bh, "fastforward");
      button.setImage(Palette.createImage("/images/fastforward.png"));
      buttons.add(button);
      
      button = new Button(w/2 - bw/2 - bx, by, bw, bh, "rewind");
      button.setImage(Palette.createImage("/images/rewind.png"));
      buttons.add(button);
      bx += bw;

      button = new Button(w/2 + bx - bw/2, by, bw, bh, "stepforward");
      button.setImage(Palette.createImage("/images/stepforward.png"));
      buttons.add(button);
      
      button = new Button(w/2 - bx - bw/2, by, bw, bh, "stepback");
      button.setImage(Palette.createImage("/images/stepback.png"));
      buttons.add(button);

      bx = 10;
      button = new Button(bx, by, bw, bh, "restart");
      button.setImage(Palette.createImage("/images/restart.png"));
      buttons.add(button);
      
      bx = w - 10 - bw;
      button = new Button(bx, by, bw, bh, "fullscreen");
      button.setImage(Palette.createImage("/images/fullscreen.png"));
      buttons.add(button);
      fullButton = button;
      
      button = new Button(bx, by, bw, bh, "partscreen");
      button.setImage(Palette.createImage("/images/partscreen.png"));
      buttons.add(button);
      button.setVisible(false);
      button.setEnabled(false);
      partButton = button;

      bw = 250;
      bh = 10;
      bx = w/2 - bw/2;
      by = h - bh/2 - 10;

      button = new Button(bx - 10, by - 10, 20, 20, "ball");
      button.setImage(Palette.createImage("/images/ball.png"));
      buttons.add(button);
      scrubButton = button;
   }
   
   
   public boolean isPlaying() {
      return this.pstate > 0;
   }
   
   
   public boolean isPaused() {
      return this.pstate == 0;
   }
   
   
   public boolean isFullscreen() {
      return this.fullscreen;
   }
   

   public void draw(Graphics2D g) {
      
      int w = getWidth();
      int h = getHeight();

      //Color background = new Color(8, 145, 145);
      //Color background = new Color(35, 69, 102);
      Color background = new Color(22, 49, 79);
      Color foreground = Color.WHITE;
      RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, w, h, 10, 10);
      g.setColor(background);
      g.fill(rect);
      g.setStroke(Palette.STROKE1);
      g.setColor(foreground);
      g.draw(rect);

      //---------------------------------------------
      // Draw speedup
      //---------------------------------------------
      g.setFont(Palette.FONT_H1);
      if (pstate > 1) {
         g.drawString("x" + pstate, w/2 - 160, h - 10);
      }
      else if (pstate < -1) {
         g.drawString("x" + (pstate * -1), w/2 - 160, h - 10);
      }         
      
      //---------------------------------------------
      // Draw tick counter
      //---------------------------------------------
      g.setFont(Palette.FONT_BODY);
      String s = "tick: " + model.getPlayHead();
      int fw = g.getFontMetrics().stringWidth(s);
      g.drawString(s, w - fw - 10, h - 15);
      
      //---------------------------------------------
      // Draw the scrub bar
      //---------------------------------------------
      int bw = 250;
      int bh = 10;
      int bx = w/2 - bw/2;
      int by = h - bh - 10;
      g.setColor(Color.DARK_GRAY);
      g.fillRect(bx, by, bw, bh);

      SimStream stream = model.getStream();
      float scale = (float)bw / scrubMax;
      int min = (int)(stream.getMinIndex() * scale);
      int max = (int)(stream.getMaxIndex() * scale);

      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(bx + min, by, max - min, bh);

      g.setColor(Color.GRAY);
      g.drawRect(bx, by, bw, bh);


      //---------------------------------------------
      // Move the play head
      //---------------------------------------------
      int index = model.getPlayHead();
      bx += (int)(index * scale);
      by += bh / 2;
      scrubButton.reshape(bx - 10, by - 10, 20, 20);

      for (Button button : buttons) {
         button.setDown(false);
      }

      if (target != null && target.containsTouch(touchX, touchY)) {
         target.setDown(true);
      }

      scrubButton.setDown(false);
      for (Button button : buttons) {
         button.draw(g);
      }
   }

   boolean flip = false;
   protected void movePlayHead(int delta) {
      if (flip && Math.abs(delta) == 1) return;
      if (Math.abs(delta) > 1) delta /= 2;
      SimStream stream = model.getStream();
      int index = model.getPlayHead() + delta;
      if (!model.isLoaded()) return;
      while (index > stream.getMaxIndex()) {
         model.tick();
      }
      model.setPlayHead(index);
      if (index <= stream.getMinIndex()) pstate = 0;
   }

   public void animate() {
      flip = !flip;
      SimStream stream = model.getStream();

      // Adjust size of scrub bar if necessary
      int index = model.getPlayHead();
      if (stream.getMaxIndex() == 0) {
         scrubMax = stream.getCapacity();
      } else if (index * 1.15f > scrubMax) {
         scrubMax *= 2;
      }
      
      // fill the simulation buffer
      if (!stream.isBufferFull()) {
         //model.tick();
      }
      
      // play or fastforward
      movePlayHead(pstate);
   }

   public void startTween(String property, Tween tween) { }
   public void endTween(String property, Tween tween) { }
   public void setTweenValue(String property, Tween tween) { }
   

   public void onDown() {
      this.target = null;
      for (Button button : buttons) {
         if (button.containsTouch(touchX, touchY) && button.isEnabled()) {
            this.target = button;
            dragging = false;
            break;
         }
      }
   }
   
   public void onRelease() {
      if (target != null && target.containsTouch(touchX, touchY)) {
         onClick(target);
      }
      target = null;
   }
   
   public void onDrag() {
      if (target == scrubButton) {
         pstate = 0;
         int w = getWidth();
         int bw = 250;
         int bx = w/2 - bw/2;
         SimStream stream = model.getStream();
         float scale = (float)bw / scrubMax;
         int min = bx + (int)(stream.getMinIndex() * scale);
         int max = bx + (int)(stream.getMaxIndex() * scale);

         int index = (int)((touchX - bx) / scale);
         model.setPlayHead(index);
      }
   }
   
   public void onHover() { }

   public void onClick(Button button) {
      Main app = Main.instance;
      if ("play".equals(button.getAction())) {
         this.pstate = 1;
      } else if ("pause".equals(button.getAction())) {
         this.pstate = 0;
      } else if ("restart".equals(button.getAction())) {
         model.setup();
         app.layout(app.getWidth(), app.getHeight());
         this.pstate = 0;
      } else if ("fastforward".equals(button.getAction())) {
         if (pstate <= 0 || pstate >= 8) {
            pstate = 1;
         } else if (pstate > 0 && pstate < 8) {
            pstate *= 2;
         } 
      } else if ("rewind".equals(button.getAction())) {
         if (pstate >= 0 || pstate <= -8) {
            pstate = -1;
         } else if (pstate < 0 && pstate > -8) {
            pstate *= 2;
         }
      } else if ("stepforward".equals(button.getAction())) {
         pstate = 0;
         movePlayHead(1);
      } else if ("stepback".equals(button.getAction())) {
         pstate = 0;
         movePlayHead(-1);
      } else if ("fullscreen".equals(button.getAction())) {
         fullButton.setVisible(false);
         fullButton.setEnabled(false);
         partButton.setVisible(true);
         partButton.setEnabled(true);
         this.fullscreen = true;
         app.enterFullscreen();
         
      } else if ("partscreen".equals(button.getAction())) {
         fullButton.setVisible(true);
         fullButton.setEnabled(true);
         partButton.setVisible(false);
         partButton.setEnabled(false);
         this.fullscreen = false;
         app.exitFullscreen();
      }
      
      
      playButton.setEnabled(pstate == 0);
      playButton.setVisible(pstate == 0);
      pauseButton.setEnabled(pstate != 0);
      pauseButton.setVisible(pstate != 0);
   }
   
   public void touchDown(TouchFrame frame) {
      onDown();
   }

   public void touchDrag(TouchFrame frame) {
      onDrag();
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
      if (target == null) {
         translateInWorld(e.getX() - mouseX, e.getY() - mouseY);
      }
      this.mouseX = e.getX();
      this.mouseY = e.getY();
   }
}