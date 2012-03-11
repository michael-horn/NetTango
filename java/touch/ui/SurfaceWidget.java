/*
 * @(#) SurfaceWidget.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch.ui;

import touch.Touchable;
import java.awt.Font;
import java.awt.Color;


public abstract class SurfaceWidget extends Touchable {
   
   protected Color foreground;
   protected Color background;
   protected Font font;
   protected String text;
   protected int id;
   protected boolean enabled;


   public SurfaceWidget(String text) {
      this.background = new Color(0x22ffffff, true);
      this.foreground = Color.DARK_GRAY;
      this.font = new Font(null, Font.BOLD, 14);
      this.text = text;
      this.id = 0;
      this.enabled = false;

      setMovable(false);
      setResizable(false);
      setRotatable(false);
      setTossable(false);
   }

   public int getWidgetID() {
      return this.id;
   }

   public void setWidgetID(int id) {
      this.id = id;
   }

   public Color getForeground() {
      return this.foreground;
   }

   public void setForeground(Color foreground) {
      this.foreground = foreground;
   }

   public Color getBackground() {
      return this.background;
   }

   public void setBackground(Color background) {
      this.background = background;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      this.font = font;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }
}
