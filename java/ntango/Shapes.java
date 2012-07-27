/*
 * @(#) Shapes.java
 */
package ntango;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;


public abstract class Shapes {
   
   protected static GeneralPath wolf;
   protected static GeneralPath vector;
   protected static GeneralPath sheep_body;
   protected static GeneralPath sheep_legs;
   protected static GeneralPath sheep_eyes;
   protected static GeneralPath turtle_body;
   protected static GeneralPath turtle_shell;
   protected static GeneralPath turtle_eyes;

   public static float PATCH_SIZE = 10;

   
   public static void drawTurtleShape(Graphics2D g, Turtle turtle) {
		AffineTransform save = g.getTransform();
      g.scale(PATCH_SIZE, PATCH_SIZE);
      g.translate(turtle.getX(), turtle.getY());
      g.rotate(Math.PI * turtle.getHeading() / -180);
      
      String shape = turtle.getShape();

      if ("wolf".equalsIgnoreCase(shape)) {
         drawWolf(g, turtle);
      }
      else if ("sheep".equalsIgnoreCase(shape)) {
         drawSheep(g, turtle);
      }
      else if ("turtle".equalsIgnoreCase(shape)) {
         drawTurtle(g, turtle);
      }
      else if ("circle".equalsIgnoreCase(shape)) {
         drawCircle(g, turtle);
      }
      else {
         drawVector(g, turtle);
      }
      g.setTransform(save);
   }

   public static void drawCircle(Graphics2D g, Turtle turtle) {
      float zoom = turtle.getSize();
      Ellipse2D circ = new Ellipse2D.Float(
         -zoom/2, -zoom/2, zoom, zoom);
      g.setColor(turtle.getColor());
      g.fill(circ);
   }

   public static void drawSheep(Graphics2D g, Turtle turtle) {
      float zoom = turtle.getSize() / 20f;
      g.rotate(Math.PI/2);
      g.scale(zoom, zoom);
      g.setColor(turtle.getColor());
      g.fill(sheep_body);
      g.setColor(Color.BLACK);
      g.fill(sheep_legs);
      g.setColor(Color.WHITE);
      g.fill(sheep_eyes);
   }

   public static void drawWolf(Graphics2D g, Turtle turtle) {
      float zoom = turtle.getSize() / 18f;
      g.rotate(-Math.PI/2);
      g.scale(zoom, zoom);
      g.setColor(turtle.getColor());
      g.fill(wolf);
      float sw = 1 / (PATCH_SIZE * zoom);
      g.setColor(Color.DARK_GRAY);
      g.setStroke(new java.awt.BasicStroke(sw));
      g.draw(wolf);
   }

   public static void drawVector(Graphics2D g, Turtle turtle) {
      float zoom = turtle.getSize();
      g.scale(zoom, zoom);
      g.setColor(turtle.getColor());
      g.fill(vector);
      //g.setColor(Color.DARK_GRAY);
      //g.setStroke(new java.awt.BasicStroke(1 / (PATCH_SIZE * zoom)));
      //g.draw(vector);
   }

   public static void drawTurtle(Graphics2D g, Turtle turtle) {
      float zoom = turtle.getSize() / 25f;
      g.rotate(Math.PI);
      g.scale(zoom, zoom);
      g.setColor(Color.GREEN.darker());
      g.fill(turtle_body);
      g.setColor(Color.DARK_GRAY);
      g.setStroke(new java.awt.BasicStroke(1 / (PATCH_SIZE * zoom)));
      g.draw(turtle_body);
      g.setColor(turtle.getColor());
      g.fill(turtle_shell);
      g.setColor(Color.DARK_GRAY);
      g.draw(turtle_shell);
      g.setColor(Color.BLACK);
      g.fill(turtle_eyes);
   }

   static {
      
      wolf = new GeneralPath();
      wolf.moveTo(-7.130, 2.350);
      wolf.curveTo(-6.621, 1.467, -5.743, 0.961, -5.030, 0.300);
      wolf.curveTo(-5.322, -0.493, -6.564, -0.557, -7.130, -1.250);
      wolf.curveTo(-7.773, -2.057, -7.822, -3.983, -6.680, -4.350);
      wolf.curveTo(-5.812, -4.147, -6.166, -3.379, -5.980, -2.600);
      wolf.curveTo(-5.669, -1.338, -3.380, -2.091, -3.030, -0.800);
      wolf.curveTo(-2.740, -1.975, -3.123, -3.257, -3.280, -4.300);
      wolf.curveTo(-3.669, -4.561, -4.104, -4.794, -4.030, -5.300);
      wolf.curveTo(-3.931, -5.957, -2.930, -5.872, -2.280, -5.700);
      wolf.curveTo(-1.504, -4.247, -1.154, -2.364, -0.880, -0.400);
      wolf.curveTo(-0.179, 0.011, 0.464, 0.484, 1.470, 0.600);
      wolf.curveTo(1.837, -0.053, 1.574, -0.826, 1.870, -1.450);
      wolf.curveTo(2.068, -1.871, 2.566, -2.154, 2.570, -2.550);
      wolf.curveTo(2.585, -3.368, 1.409, -3.569, 1.120, -4.300);
      wolf.curveTo(0.493, -4.428, 0.058, -4.743, 0.170, -5.600);
      wolf.curveTo(0.582, -5.792, 1.249, -6.002, 1.820, -5.750);
      wolf.curveTo(2.398, -5.511, 2.767, -4.644, 3.270, -4.100);
      wolf.curveTo(3.672, -3.682, 4.333, -3.326, 4.470, -2.850);
      wolf.curveTo(4.647, -2.264, 4.378, -1.610, 4.370, -1.100);
      wolf.curveTo(4.735, -1.289, 4.941, -1.785, 5.320, -2.100);
      wolf.curveTo(5.637, -2.372, 6.032, -2.532, 6.370, -2.750);
      wolf.curveTo(6.323, -3.386, 6.695, -3.657, 6.520, -4.300);
      wolf.curveTo(6.382, -4.832, 5.769, -4.768, 5.870, -5.350);
      wolf.curveTo(5.960, -5.860, 6.812, -5.940, 7.570, -5.800);
      wolf.curveTo(8.294, -5.235, 7.267, -2.075, 7.220, -0.950);
      wolf.curveTo(8.620, -2.107, 9.966, -2.796, 10.720, -2.900);
      wolf.curveTo(9.570, 1.094, 8.455, 0.093, 7.270, 1.400);
      wolf.curveTo(6.886, 3.802, 6.280, 5.540, 3.570, 5.700);
      wolf.curveTo(2.780, 5.739, 1.084, 5.306, 0.220, 5.350);
      wolf.curveTo(-2.391, 5.459, -5.152, 6.800, -6.230, 5.850);
      wolf.curveTo(-5.930, 7.568, -7.499, 6.530, -8.280, 6.050);
      wolf.curveTo(-8.878, 5.669, -9.443, 5.310, -9.730, 5.000);
      wolf.curveTo(-9.584, 4.033, -11.089, 3.597, -11.180, 3.000);
      wolf.curveTo(-11.264, 2.410, -10.351, 1.833, -9.830, 1.850);
      wolf.curveTo(-9.480, 1.850, -9.122, 2.313, -8.680, 2.350);
      wolf.curveTo(-8.078, 2.383, -7.553, 1.855, -7.130, 2.350);
      wolf.closePath();

      vector = new GeneralPath();
      vector.moveTo(0, 0.5);
      vector.lineTo(0.4, -0.5);
      vector.lineTo(0, -0.25);
      vector.lineTo(-0.4, -0.5);
      vector.closePath();

      sheep_body = new GeneralPath();
      sheep_body.moveTo(6.200, 5.360);
      sheep_body.curveTo(5.129, 6.782, 3.876, 7.621, 2.085, 6.439);
      sheep_body.curveTo(0.888, 5.650, -2.057, 6.703, -2.186, 4.305);
      sheep_body.curveTo(-3.911, 5.403, -6.114, 4.650, -6.150, 2.462);
      sheep_body.curveTo(-9.186, 4.221, -10.697, -0.223, -8.580, -2.038);
      sheep_body.curveTo(-7.809, -2.699, -8.894, -4.490, -7.346, -5.375);
      sheep_body.curveTo(-5.881, -6.051, -4.980, -5.888, -3.946, -4.855);
      sheep_body.curveTo(-3.269, -6.578, -1.079, -5.978, -0.266, -4.895);
      sheep_body.curveTo(-0.108, -5.686, 1.293, -6.205, 2.004, -5.955);
      sheep_body.curveTo(2.323, -5.857, 2.575, -5.672, 2.759, -5.400);
      sheep_body.curveTo(3.162, -4.901, 3.885, -5.289, 4.484, -5.115);
      sheep_body.curveTo(5.978, -4.679, 6.961, -2.716, 6.314, -1.255);
      sheep_body.curveTo(8.376, -1.224, 7.118, 1.552, 6.349, 2.320);
      sheep_body.curveTo(5.426, 3.242, 6.520, 4.003, 6.210, 5.342);
      sheep_body.closePath();

      sheep_legs = new GeneralPath();
      sheep_legs.moveTo(2.926, -5.304);
      sheep_legs.curveTo(3.110, -5.266, 3.293, -5.228, 3.476, -5.190);
      sheep_legs.curveTo(3.522, -5.828, 4.573, -8.397, 3.436, -8.630);
      sheep_legs.curveTo(1.868, -8.951, 2.275, -5.182, 2.926, -5.304);
      sheep_legs.lineTo(-4.382, -5.140);
      sheep_legs.curveTo(-3.591, -5.021, -3.816, -5.292, -3.688, -6.034);
      sheep_legs.curveTo(-3.616, -6.336, -3.537, -6.635, -3.458, -6.935);
      sheep_legs.curveTo(-3.374, -7.253, -3.092, -8.097, -3.442, -8.286);
      sheep_legs.curveTo(-5.433, -9.358, -5.064, -5.112, -4.382, -5.140);
      sheep_legs.closePath();
      sheep_legs.moveTo(6.184, 4.568);
      sheep_legs.curveTo(5.411, 4.241, 4.561, 4.315, 3.812, 3.868);
      sheep_legs.curveTo(2.233, 2.927, 3.588, 2.443, 3.852, 1.386);
      sheep_legs.curveTo(3.947, 0.961, 3.604, 0.518, 3.762, 0.035);
      sheep_legs.curveTo(3.975, -1.127, 5.047, -1.799, 6.052, -2.218);
      sheep_legs.curveTo(7.586, -2.857, 9.110, -1.202, 9.248, 0.299);
      sheep_legs.curveTo(9.452, 2.506, 6.982, 2.918, 6.184, 4.568);
      sheep_legs.closePath();

      sheep_eyes = new GeneralPath();
      sheep_eyes.moveTo(5.412, 3.216);
      sheep_eyes.curveTo(5.412, 3.034, 5.547, 2.886, 5.712, 2.886);
      sheep_eyes.curveTo(5.878, 2.886, 6.012, 3.034, 6.012, 3.216);
      sheep_eyes.curveTo(6.012, 3.398, 5.878, 3.546, 5.712, 3.546);
      sheep_eyes.curveTo(5.547, 3.546, 5.412, 3.398, 5.412, 3.216);
      sheep_eyes.closePath();
      sheep_eyes.moveTo(4.512, 2.706);
      sheep_eyes.curveTo(4.512, 2.524, 4.647, 2.376, 4.812, 2.376);
      sheep_eyes.curveTo(4.978, 2.376, 5.112, 2.524, 5.112, 2.706);
      sheep_eyes.curveTo(5.112, 2.888, 4.978, 3.036, 4.812, 3.036);
      sheep_eyes.curveTo(4.647, 3.036, 4.512, 2.888, 4.512, 2.706);
      sheep_eyes.closePath();
      

      turtle_body = new GeneralPath();
      turtle_body.moveTo(0.144, -10.224);
      turtle_body.curveTo(-0.452, -10.406, -1.127, -10.260, -1.556, -9.824);
      turtle_body.curveTo(-2.401, -8.966, -2.361, -7.290, -1.906, -5.924);
      turtle_body.curveTo(-2.361, -5.445, -2.853, -5.003, -3.506, -4.724);
      turtle_body.curveTo(-3.691, -5.514, -3.912, -5.804, -4.609, -6.179);
      turtle_body.curveTo(-6.859, -7.389, -7.359, -6.829, -6.959, -4.979);
      turtle_body.curveTo(-6.665, -4.340, -6.859, -3.529, -4.706, -2.874);
      turtle_body.curveTo(-5.437, -0.861, -6.006, 1.546, -5.756, 4.376);
      turtle_body.curveTo(-7.295, 4.443, -7.753, 5.568, -7.959, 6.071);
      turtle_body.curveTo(-8.412, 7.178, -8.650, 7.851, -7.109, 7.971);
      turtle_body.curveTo(-5.459, 7.921, -5.609, 7.526, -4.559, 6.371);
      turtle_body.curveTo(-4.446, 6.246, -3.209, 8.071, -1.056, 8.726);
      turtle_body.curveTo(-0.949, 9.884, -1.144, 10.501, -1.140, 11.388);
      turtle_body.curveTo(0.272, 11.201, 0.401, 10.234, 0.344, 8.576);
      turtle_body.curveTo(1.241, 8.290, 2.672, 7.480, 3.441, 7.065);
      turtle_body.curveTo(3.956, 7.891, 4.579, 8.309, 5.979, 8.511);
      turtle_body.curveTo(7.388, 8.571, 7.446, 8.471, 7.291, 7.071);
      turtle_body.curveTo(7.292, 7.399, 6.887, 5.471, 4.741, 5.371);
      turtle_body.curveTo(5.213, 2.884, 4.813, -0.964, 4.044, -2.824);
      turtle_body.curveTo(5.666, -3.629, 6.207, -3.847, 6.403, -4.779);
      turtle_body.curveTo(6.766, -6.504, 6.491, -6.754, 4.685, -6.472);
      turtle_body.curveTo(4.061, -6.375, 3.241, -5.181, 2.894, -4.624);
      turtle_body.curveTo(2.433, -5.180, 1.498, -5.262, 1.044, -5.824);
      turtle_body.curveTo(2.228, -7.092, 1.629, -9.771, 0.144, -10.224);
      turtle_body.closePath();

      turtle_shell = new GeneralPath();
      turtle_shell.moveTo(-4.160, -4.014);
      turtle_shell.curveTo(-3.107, -8.089, 2.875, -6.427, 4.187, -3.514);
      turtle_shell.curveTo(5.652, -1.053, 6.606, 3.829, 4.416, 6.057);
      turtle_shell.curveTo(1.731, 9.898, -1.742, 9.988, -4.787, 6.435);
      turtle_shell.curveTo(-7.171, 3.654, -5.633, -1.079, -4.160, -4.014);
      turtle_shell.closePath();

      turtle_eyes = new GeneralPath();
      turtle_eyes.moveTo(-0.684, -8.430);
      turtle_eyes.curveTo(-0.723, -8.502, -0.923, -8.472, -1.130, -8.361);
      turtle_eyes.curveTo(-1.337, -8.251, -1.473, -8.102, -1.434, -8.029);
      turtle_eyes.curveTo(-1.395, -7.956, -1.195, -7.986, -0.988, -8.097);
      turtle_eyes.curveTo(-0.781, -8.208, -0.645, -8.357, -0.684, -8.430);
      turtle_eyes.closePath();
      turtle_eyes.moveTo(0.837, -8.075);
      turtle_eyes.curveTo(0.867, -8.152, 0.714, -8.284, 0.496, -8.369);
      turtle_eyes.curveTo(0.277, -8.454, 0.075, -8.460, 0.045, -8.383);
      turtle_eyes.curveTo(0.015, -8.307, 0.168, -8.175, 0.387, -8.090);
      turtle_eyes.curveTo(0.605, -8.005, 0.807, -7.998, 0.837, -8.075);
      turtle_eyes.closePath();
      /*
      AffineTransform tform = new AffineTransform();
      tform.scale(0.05, 0.05);
      tform.translate(-170, -210);
      PathIterator pi = turtle_eyes.getPathIterator(tform);
      double points [] = new double[6];
      while (!pi.isDone()) {
         int seg = pi.currentSegment(points);
         if (seg == pi.SEG_MOVETO) {
            System.out.println(
               "turtle_eyes.moveTo(" +
               String.format("%.3f", points[0]) + ", " +
               String.format("%.3f", points[1]) + ");");
         } else if (seg == pi.SEG_CUBICTO) {
            System.out.println(
               "turtle_eyes.curveTo(" +
               String.format("%.3f", points[0]) + ", " +
               String.format("%.3f", points[1]) + ", " +
               String.format("%.3f", points[2]) + ", " +
               String.format("%.3f", points[3]) + ", " +
               String.format("%.3f", points[4]) + ", " +
               String.format("%.3f", points[5]) + ");");
         } else if (seg == pi.SEG_CLOSE) {
            System.out.println("turtle_eyes.closePath();");
         } else if (seg == pi.SEG_LINETO) {
            System.out.println(
               "turtle_eyes.lineTo(" +
               String.format("%.3f", points[0]) + ", " +
               String.format("%.3f", points[1]) + ");");
         }
         pi.next();
      }
      */
   }
}