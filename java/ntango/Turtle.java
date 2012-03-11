/*
 * @(#) Turtle.java
 */
package ntango;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class Turtle {

   protected int id;
   protected float x;
   protected float y;
   protected float size;
   protected float heading;
   protected Color color;
   protected String shape;
   protected String breed;
   protected float lthick;
   protected String label;
   protected boolean dead;


   public Turtle(int id) {
      this.id = id;
      this.x = 0;
      this.y = 0;
      this.size = 1;
      this.heading = 0;
      this.color = Color.RED;
      this.shape = "turtle";
      this.breed = "turtle";
      this.lthick = 0;
      this.label = null;
      this.dead = false;
   }

   public Turtle(org.nlogo.api.Turtle t) {
      this.id = (int)t.id();
      this.x = (float)t.xcor();
      this.y = (float)t.ycor();
      this.size = (float)t.size();
      this.heading = (float)t.heading();
      this.color = org.nlogo.api.Color.getColor(t.color());
      this.shape = t.shape();
      this.breed = t.getBreed().printName();
      this.lthick = (float)t.lineThickness();
      this.label = t.labelString();
      this.dead = false;
   }

   public Turtle(Turtle other) {
      this.id = other.getID();
      this.x = other.getX();
      this.y = other.getY();
      this.size = other.getSize();
      this.heading = other.getHeading();
      this.color = other.getColor();
      this.shape = other.getShape();
      this.breed = other.getBreed();
      this.lthick = other.getLineThickness();
      this.label = other.getLabel();
      this.dead = other.isDead();
   }

   public int getID() {
      return this.id;
   }

   public float getHeading() {
      return this.heading;
   }

   public void setHeading(float heading) {
      this.heading = heading;
   }

   public float getLineThickness() {
      return this.lthick;
   }

   public void setLineThickness(float lthick) {
      this.lthick = lthick;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public boolean hasLabel() {
      return this.label != null;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public float getSize() {
      return this.size;
   }

   public void setSize(float size) {
      this.size = size;
   }

   public String getShape() {
      return this.shape;
   }

   public void setShape(String shape) {
      this.shape = shape;
   }

   public String getBreed() {
      return this.breed;
   }

   public void setBreed(String breed) {
      this.breed = breed;
   }

   public boolean isDead() {
      return this.dead;
   }

   public void setDead(boolean dead) {
      this.dead = dead;
   }

   public boolean containsPoint(float tx, float ty) {
      return (Math.abs(x - tx) <= size/2 &&
              Math.abs(y - ty) <= size/2);
   }

   public void draw(Graphics2D g, float psize) {
      Shapes.drawTurtleShape(g, this);
      
      if (isDead()) {
         AffineTransform save = g.getTransform();
         g.translate(x * psize, y * psize);
         int w = (int)psize;
         g.setColor(Color.RED);
         g.setStroke(Palette.STROKE3);
         g.drawLine(-w/2, -w/2, w/2, w/2);
         g.drawLine(-w/2, w/2, w/2, -w/2);
         g.setTransform(save);
      }
	}

   public void drawWatch(Graphics2D g, float psize, Color wcolor) {
		AffineTransform save = g.getTransform();
      int w = (int)(psize * size * 1.5);
      g.translate(x * psize, y * psize);
      g.setColor(new Color(0x33ffffff, true));
      g.fillOval(-w/2, -w/2, w, w);
      //g.setColor(Color.WHITE);
      g.setColor(wcolor);
      g.setStroke(Palette.STROKE2);
      g.drawOval(-w/2, -w/2, w, w);
      g.setTransform(save);
   }
}
