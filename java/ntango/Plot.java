/*
 * @(#) Plot.java
 */
package ntango;

import touch.*;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;
   

public class Plot extends Touchable {

   protected static int TMARGIN = 25;
   protected static int BMARGIN = 25;
   protected static int LMARGIN = 35;
   protected static int RMARGIN = 85;
   
   protected List<Pen> pens;
   protected String title;
   protected String xlabel;
   protected String ylabel;
   protected float minx;
   protected float maxx;
   protected float miny;
   protected float maxy;

   
   public Plot() {
      this.title = "Plot";
      this.xlabel = "time";
      this.ylabel = "";
      this.minx = 0;
      this.maxx = 100;
      this.miny = 0;
      this.maxy = 100;
      this.pens = new java.util.ArrayList<Pen>();
      setWidth(400);
      setHeight(150);
      setVisible(true);
      setMovable(true);
      setResizable(false);
      setRotatable(false);
   }

   public Plot(org.nlogo.plot.Plot plot) {
      this();
      setTitle(plot.name());
      setMinX((float)plot.xMin());
      setMaxX((float)plot.xMax());
      setMinY((float)plot.yMin());
      setMaxY((float)plot.yMax());

      /*
      java.lang.reflect.Method [] methods = plot.getClass().getMethods();
      for (int i=0; i<methods.length; i++) {
         System.out.println(methods[i]);
      }
      */
      for (org.nlogo.plot.PlotPen npen : plot.pens()) {
         Pen pen = new Pen(npen);
         pen.setStartX(getMinX());
         pens.add(pen);
      }
   }

   public Pen getPen(String name) {
      for (Pen pen : pens) {
         if (pen.getName().equals(name)) {
            return pen;
         }
      }
      return null;
   }

   public void update(org.nlogo.plot.Plot plot) {
      for (org.nlogo.plot.PlotPen npen : plot.pens()) {
         Pen pen = getPen(npen.name());
         if (pen != null) {
            List points = npen.points();
            int size = points.size();
            if (size > 0) {
               org.nlogo.plot.PlotPoint point =
               (org.nlogo.plot.PlotPoint)points.get(size - 1);
               pen.plot((float)point.y());
            }
         }
      }
   }

   public void draw(Graphics2D g) {
      setBounds();

      float _maxx = (int)(maxx * 1.2);
      
      int w = getWidth();
      int h = getHeight();
      int iw = w - LMARGIN - RMARGIN;
      int ih = h - TMARGIN - BMARGIN;
      
      g.setColor(new Color(0x66000000, true));
      g.fillRect(0, 0, w, h);
      g.setColor(Color.LIGHT_GRAY);
      g.drawRect(0, 0, w, h);

      g.setFont(new Font(null, 0, 14));
      int fw = g.getFontMetrics().stringWidth(title);
      g.drawString(title, LMARGIN + iw/2 - fw/2, TMARGIN - 5);

      g.setFont(new Font(null, 0, 11));
      
      String s = String.valueOf((int)minx);
      g.drawString(s, LMARGIN, h - 10);
      s = String.valueOf((int)_maxx);
      fw = g.getFontMetrics().stringWidth(s);
      g.drawString(s, w - RMARGIN - fw, h - 10);

      s = String.valueOf((int)maxy);
      fw = g.getFontMetrics().stringWidth(s);
      g.drawString(s, LMARGIN - fw - 4, TMARGIN + 12);
      s = String.valueOf((int)miny);
      fw = g.getFontMetrics().stringWidth(s);
      g.drawString(s, LMARGIN - fw - 4, h - BMARGIN - 1);

      g.setFont(new Font(null, 0, 12));
      s = getXLabel();
      fw = g.getFontMetrics().stringWidth(s);
      g.drawString(s, LMARGIN + iw/2 - fw/2, h - 10);

      AffineTransform save = g.getTransform();
      g.rotate(-Math.PI / 2);
      g.drawString(getYLabel(), -TMARGIN - ih/2 - fw/2, LMARGIN - 7);
      g.setTransform(save);

      g.setFont(new Font(null, 0, 11));
      int ty = TMARGIN + 12;
      for (Pen pen : pens) {
         s = pen.getName();
         g.setColor(pen.getColor());
         g.fillRect(w - RMARGIN + 4, ty - 9, 12, 12);
         g.setColor(Color.LIGHT_GRAY);
         g.drawRect(w - RMARGIN + 4, ty - 9, 12, 12);
         g.drawString(s, w - RMARGIN + 20, ty);
         ty += 18;
      }
      
      int x = LMARGIN;
      int y = TMARGIN;
      w -= LMARGIN + RMARGIN;
      h -= TMARGIN + BMARGIN;

      g.setColor(Color.WHITE);
      g.fillRect(x, y, w, h);
      g.setColor(Color.GRAY);
      //g.drawRect(x, y, w, h);
      float scaleX = w / (_maxx - minx);
      float scaleY = h / (maxy - miny);

      g.translate(LMARGIN, ih + TMARGIN);
      g.scale(1, -1);
      for (Pen pen : pens) {
         pen.draw(g, scaleX, scaleY);
      }
      g.setTransform(save);
   }

   public void clear() {
      for (Pen pen : pens) {
         pen.clear();
      }
   }

   protected void setBounds() {
      for (Pen pen : pens) {
         this.minx = Math.min(pen.getMinX(), minx);
         this.maxx = Math.max(pen.getMaxX(), maxx);
         this.miny = Math.min(pen.getMinY(), miny);
         this.maxy = Math.max(pen.getMaxY(), maxy);
      }
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getXLabel() {
      return this.xlabel;
   }

   public void setXLabel(String xlabel) {
      this.xlabel = xlabel;
   }

   public String getYLabel() {
      return this.ylabel;
   }

   public void setYLabel(String ylabel) {
      this.ylabel = ylabel;
   }

   public float getMinX() {
      return this.minx;
   }

   public void setMinX(float minx) {
      this.minx = minx;
   }

   public float getMaxX() {
      return this.maxx;
   }

   public void setMaxX(float maxx) {
      this.maxx = maxx;
   }

   public float getMinY() {
      return this.miny;
   }

   public void setMinY(float miny) {
      this.miny = miny;
   }

   public float getMaxY() {
      return this.maxy;
   }

   public void setMaxY(float maxy) {
      this.maxy = maxy;
   }

   public void addPen(String spec) {
      String [] params = spec.split(" ");
      Pen pen = new Pen(params[0]);
      // pen.setInterval(parseFloat(params[1]));
      // pen.setMode(params[2]);
      // pen.setColor(netlogo.Color.color(params[3]));
      // pen.showInLegend(params[4]);
      pens.add(pen);
   }

   int mouseX;
   int mouseY;
   
   public void mousePressed(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
   }
   
   public void mouseReleased(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
   }
   
   public void mouseMoved(MouseEvent e) {
      this.mouseX = e.getX();
      this.mouseY = e.getY();
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
   }
   
   public void mouseDragged(MouseEvent e) {
      this.touchX = screenToObjectX(e.getX(), e.getY());
      this.touchY = screenToObjectY(e.getX(), e.getY());
      translateInWorld(e.getX() - mouseX, e.getY() - mouseY);
      this.mouseX = e.getX();
      this.mouseY = e.getY();
   }
}