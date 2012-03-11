/*
 * @(#) SurfaceFrame.java
 * 
 * Michael Horn
 * AmphibiaWeb Visualizer
 * Northwestern University
 */
package touch;

import tween.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;



public abstract class SurfaceFrame extends JPanel
   implements WindowListener, ComponentListener, TouchDelegator, TouchListener, ActionListener {

   /** ID number for the default widget layer */
   public static final int DEFAULT_LAYER = 0;

   /** Save the graphics devices to create compatible bitmaps later */
   public static GraphicsDevice device = null;

   /** Swing application frame */
   protected JFrame frame;

   /** Touch event dispatcher */
   protected SurfaceConnector surface;

   /** Touchable background area */
   protected Touchable screen;

   /** Widgets can be drawn in one of several photoshop-type layers */
   protected List<WidgetLayer> layers;

   /** World to screen transform */
   protected AffineTransform w2s;

   /** Application config properties */
   protected Properties props;

   /** Tween manager */
   protected TweenManager tweens;

   protected double zoom = 1;

   public SurfaceFrame(String title) {
      super(true);
      this.screen = new Touchable();
      this.layers = new java.util.ArrayList<WidgetLayer>();
      this.w2s = new AffineTransform();
      this.tweens = new TweenManager();


      //--------------------------------------------------
      // Load the configuration properties
      //--------------------------------------------------
      try {
         this.props = new Properties();
         this.props.load(new FileInputStream("config.properties"));
      } catch (IOException iox) {
         iox.printStackTrace();
         System.exit(1);
      }

      
      //--------------------------------------------------
      // Create MS Surface connection
      //--------------------------------------------------
      this.surface = new SurfaceConnector(this,
                                          getProperty("surface.host"),
                                          getIntProperty("surface.port"));

      
      //--------------------------------------------------
      // Configure the top left touch coordinate
      //--------------------------------------------------
      TouchEvent.LEFT = getIntProperty("surface.x");
      TouchEvent.TOP  = getIntProperty("surface.y");

      
      //--------------------------------------------------
      // Open frame on secondary display if possible
      //--------------------------------------------------
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] gs = ge.getScreenDevices();
      int display = getIntProperty("java.display") - 1;

      if (display >= 0 && display < gs.length) {
         this.frame = new JFrame(title, gs[display].getDefaultConfiguration());
         SurfaceFrame.device = gs[display];
      } else {
         this.frame = new JFrame(title);
         SurfaceFrame.device = ge.getDefaultScreenDevice();
      }

      //--------------------------------------------------
      // Set up the frame.
      //--------------------------------------------------
      setOpaque(true);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setContentPane(this);

      /* For MS Surface deployment. */
      if (getBooleanProperty("app.fullscreen")) {
         frame.setUndecorated(true);
         frame.pack();
         frame.setVisible(true);
         frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      }
      else {
         frame.setPreferredSize(
            new java.awt.Dimension(
               getIntProperty("app.width"),
               getIntProperty("app.height")));
         frame.pack();
         frame.setVisible(true);
         frame.setExtendedState(JFrame.NORMAL);
      }

      requestFocusInWindow();

      //--------------------------------------------------
      // Define the default widget layer
      //--------------------------------------------------
      this.layers.add(new WidgetLayer());

      //--------------------------------------------------
      // Add event listeners
      //--------------------------------------------------
      addMouseListener(surface);
      addMouseMotionListener(surface);
      addComponentListener(this);
      frame.addWindowListener(this);
      screen.setListener(this);

   }

   public Properties getProperties() {
      return this.props;
   }

   public String getProperty(String key) {
      return this.props.getProperty(key);
   }

   public int getIntProperty(String key) {
      try {
         return Integer.parseInt(props.getProperty(key));
      } catch (NumberFormatException nfx) {
         return 0;
      }
   }

   public boolean getBooleanProperty(String key) {
      String p = props.getProperty(key);
      return ("true".equalsIgnoreCase(p));
   }

   /******************************************************************/
   /* SUBCLASS EVENT CALLBACKS */
   /******************************************************************/
   protected void drawBackground(Graphics2D g, int w, int h) { }

   protected void drawForeground(Graphics2D g, int w, int h) { }

   protected void drawContent(Graphics2D g, int w, int h) { }

   protected abstract void animate();

   protected void shutdown() { }

   protected void startup(int w, int h) { }

   protected void resized(int w, int h) { }


   /**
    * Adds count widget layers
    */
   public void addLayers(int count) {
      for (int i = 0; i < count; i++) {
         this.layers.add(new WidgetLayer());
      }
   }

   /**
    * Adds an object to the default layer
    */
   public void addTouchable(Touchable touchable) {
      addTouchable(touchable, DEFAULT_LAYER);
   }

   /**
    * Adds an object to the given layer
    */
   public void addTouchable(Touchable touchable, int layer) {
      layers.get(layer).addWidget(touchable);
   }

   public void clearWidgets(int layer) {
      layers.get(layer).clearWidgets();
   }

   public void enableWidgets(int layer) {
      layers.get(layer).enableWidgets();
   }

   public void disableWidgets(int layer) {
      layers.get(layer).disableWidgets();
   }

   public boolean isTopMost(Touchable touchable) {
      for (WidgetLayer layer : layers) {
         if (layer.isTopWidget(touchable)) {
            return true;
         }
      }
      return false;
   }

   public Touchable getTopMost(int layer) {
      return layers.get(layer).getTopWidget();
   }

   public List<Touchable> getWidgets(int layer) {
      return layers.get(layer).getWidgets();
   }

   protected void paintComponent(Graphics graphics) {
      int w = getWidth();
      int h = getHeight();

      Graphics2D g = (Graphics2D) graphics;
      g.setRenderingHint(
         RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);

      // draw background
      drawBackground(g, w, h);

      AffineTransform save = g.getTransform();
      g.transform(w2s);
      drawContent(g, w, h);
      g.setTransform(save);
      
      // draw foreground
      drawForeground(g, w, h);
      
      // draw floating objects on top of everything else
      for (WidgetLayer layer : layers) {
         layer.draw(g);
      }
   }

   /******************************************************************/
   /* TOUCH EVENTS */
   /******************************************************************/
   public void startTouchFrame(int touch_count) {
      for (WidgetLayer layer : layers) {
         layer.startTouchFrame();
      }
      screen.startTouchFrame();
   }

   public void endTouchFrame() {
      tweens.animate();
      screen.endTouchFrame();
      for (WidgetLayer layer : layers) {
         layer.endTouchFrame();
         layer.animate();
         layer.cleanup(); // removes any closed widgets
      }
      animate();
      repaint();
   }

   public TouchFrame getTouchFrame() {
      return screen.getTouchFrame();
   }

   public Touchable findTouchTarget(TouchEvent e) {
      for (int i = layers.size() - 1; i >= 0; i--) {
         Touchable t = layers.get(i).findTouchTarget(e);
         if (t != null) {
            return t;
         }
      }
      if (containsTouch(e)) {
         return screen;
      } else {
         return null;
      }
   }

   public void closeApplication() {
      shutdown();
      surface.stop();
      frame.setVisible(false);
      frame.dispose();
      System.exit(0);
   }

   /******************************************************************/
   /* WORLD TRANSFORM */
   /******************************************************************/
   protected static double[] temp = new double[2];

   public Point2D worldToScreen(Point2D p) {
      return w2s.transform(p, null);
   }

   public Shape worldToScreen(Shape shape) {
      return w2s.createTransformedShape(shape);
   }

   public float worldToScreenX(float x, float y) {
      temp[0] = x;
      temp[1] = y;
      w2s.transform(temp, 0, temp, 0, 1);
      return (float) temp[0];
   }

   public float worldToScreenY(float x, float y) {
      temp[0] = x;
      temp[1] = y;
      w2s.transform(temp, 0, temp, 0, 1);
      return (float) temp[1];
   }

   public float screenToWorldX(float x, float y) {
      try {
         temp[0] = x;
         temp[1] = y;
         w2s.inverseTransform(temp, 0, temp, 0, 1);
         return (float) temp[0];
      } catch (NoninvertibleTransformException ntx) {
         return 0;
      }
   }

   public float screenToWorldY(float x, float y) {
      try {
         temp[0] = x;
         temp[1] = y;
         w2s.inverseTransform(temp, 0, temp, 0, 1);
         return (float) temp[1];
      } catch (NoninvertibleTransformException ntx) {
         return 0;
      }
   }

   protected void rosize(double scale, double theta, double cx, double cy) {
      this.zoom *= scale;
      w2s.preConcatenate(AffineTransform.getTranslateInstance(-cx, -cy));
      w2s.preConcatenate(AffineTransform.getScaleInstance(scale, scale));
      w2s.preConcatenate(AffineTransform.getRotateInstance(theta));
      w2s.preConcatenate(AffineTransform.getTranslateInstance(cx, cy));
   }

   public void zoom(double scale, double cx, double cy) {
      rosize(scale, 0, cx, cy);
   }

   public void zoom(double scale) {
      zoom(scale, getWidth() * 0.5, getHeight() * 0.5);
   }

   public void pan(float dx, float dy) {
      w2s.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
   }

   public void rotate(double theta, double cx, double cy) {
      rosize(1, theta, cx, cy);
   }

   public float getZoomFactor() {
      return (float) this.zoom;
   }

   /******************************************************************/
   /* WINDOW EVENTS */
   /******************************************************************/
   public void windowClosing(WindowEvent e) {
      closeApplication();
   }

   public void windowOpened(WindowEvent e) {
      //frame.setAlwaysOnTop(true);
      screen.setWidth(getWidth());
      screen.setHeight(getHeight());
      screen.setPosition(0, 0);
      screen.setMovable(false);
      screen.setResizable(false);
      screen.setRotatable(false);
      startup(getWidth(), getHeight());
      surface.start();
   }

   public void windowActivated(WindowEvent e) {
   }

   public void windowClosed(WindowEvent e) {
   }

   public void windowDeactivated(WindowEvent e) {
   }

   public void windowDeiconified(WindowEvent e) {
   }

   public void windowIconified(WindowEvent e) {
   }

   public void componentHidden(ComponentEvent e) { }
   public void componentMoved(ComponentEvent e) { }
   public void componentResized(ComponentEvent e) {
      resized(getWidth(), getHeight());
   }
   
   public void componentShown(ComponentEvent e) { }

   public void actionPerformed(ActionEvent e) { }
}
