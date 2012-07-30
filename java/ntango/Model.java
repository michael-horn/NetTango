/*
 * @(#) Model.java
 */
package ntango;

import org.nlogo.api.World;
import org.nlogo.headless.HeadlessWorkspace;
import org.nlogo.plot.PlotPoint;

import java.awt.Color;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Map;
import java.util.Set;
import touch.ui.*;

public class Model implements ButtonListener {

   protected HeadlessWorkspace workspace;
   protected SimStream stream;
   protected int findex;  // index of the visible frame
   protected Main app;
   protected boolean loaded = false;
   protected Map<Integer, Color> watches;
   protected String name;


   public static Color [] WATCH_COLORS = {
      Color.RED,
      Color.YELLOW,
      Color.GREEN,
      Color.BLUE,
      Color.MAGENTA,
      Color.CYAN,
      Color.PINK };

   public static int wcindex = 0;
   
   
   public Model(Main app) throws Exception {
      this.workspace = HeadlessWorkspace.newInstance();
      this.stream = new SimStream(1000);
      this.findex = 0;
      this.app = app;
      this.loaded = false;
      this.watches = new java.util.HashMap<Integer, Color>();
      this.name = "";
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public SimStream getStream() {
      return this.stream;
   }

   public SimFrame getCurrentFrame() {
      return stream.getFrame(findex);
   }

   public int getPlayHead() {
      return findex;
   }

   public void setPlayHead(int i) {
      i = Math.max(i, stream.getMinIndex());
      i = Math.min(i, stream.getMaxIndex());
      this.findex = i;
   }

   private void clear() {
      stream.clear();
      watches.clear();
      findex = 0;
   }

   public synchronized void load(String filename) {
      try {
         clear();
         this.workspace = HeadlessWorkspace.newInstance();
         this.workspace.open(filename);

         BufferedReader in = new BufferedReader(new FileReader(filename));
         String line;
         while ((line = in.readLine()) != null) {
            if (line.startsWith("SLIDER")) {
               initSlider(in);
            } else if (line.startsWith("SWITCH")) {
               initSwitch(in);
            } else if (line.startsWith("PLOT")) {
               initPlot(in);
            }
         }
         in.close();

         this.loaded = true;
         
      } catch (Exception x) {
         x.printStackTrace();
      }
   }

   public synchronized void setup() {
      try {
         clear();
         workspace.command("setup");
         stream.addFrame(new SimFrame(workspace.world()));
      } catch (Exception x) {
         x.printStackTrace();
      }
   }

   public void tick() {
      if (!isLoaded()) return;
      try {
         if (!stream.isBufferFull() || findex > stream.getMinIndex()) {
            workspace.command("go");
            SimFrame last = stream.getLastFrame();
            SimFrame frame = new SimFrame(workspace.world());
            if (last != null) {
               frame.markDeadTurtles(last);
            }
            stream.addFrame(frame);

            String [] names = workspace.plotManager().getPlotNames();
            for (int i = 0; i<names.length; i++) {
               Plot plot = app.getPlot(names[i]);
               if (plot != null) {
                  plot.update(workspace.plotManager().getPlot(names[i]));
               }
               //.pens().iterator(): pen.points() x() y()
            }
         }
      } catch (Exception x) {
         x.printStackTrace();
      }
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setParameter(String param, String value) {
      try {
         workspace.command("set " + param + " " + value);
         setPlayHead(stream.getMaxIndex());
      }
      catch (Exception x) {
         x.printStackTrace();
      }
   }

   public void setParameter(String name, boolean b) {
      setParameter(name, String.valueOf(b));
   }

   public void setParameter(String name, int v) {
      setParameter(name, String.valueOf(v));
   }
   
   
   public void doTouchDown(float touchX, float touchY, int touchId) {
      if (isLoaded() && workspace.isValidIdentifier("touch-down")) {
         workspace.command("touch-down " + touchX + " " + touchY + " " + touchId);
      } else {
         System.out.println("Ignoring touch-down event");
      }
   }

   public void doTouchUp(float touchX, float touchY, int touchId) {
      if (isLoaded() && workspace.isValidIdentifier("touch-up")) {
         workspace.command("touch-up " + touchX + " " + touchY + " " + touchId);
         System.out.println("touch-up " + touchX + " " + touchY + " " + touchId);
      } else {
         System.out.println("Ignoring touch-up event");
      }
   }

   public void doTouchDrag(float touchX, float touchY, int touchId) {
      if (isLoaded() && workspace.isValidIdentifier("touch-down")) {
         workspace.command("touch-drag " + touchX + " " + touchY + " " + touchId);
      } else {
         System.out.println("Ignoring touch-drag event");
      }
   }

   public void addWatch(Turtle t) {
      if (t != null) {
         SimFrame frame = getCurrentFrame();
         int count = 0;
         for (Integer id : watches.keySet()) {
            if (frame.hasTurtle(id.intValue())) {
               count++;
            }
         }
         if (count < 4) {
            this.watches.put(t.getID(), getNextWatchColor());
         }
      }
   }

   public void toggleWatch(Turtle t) {
      if (t != null) {
         if (watches.containsKey(t.getID())) {
            watches.remove(t.getID());
         } else {
            addWatch(t);
         }
      }
   }

   public Set<Integer> getWatchList() {
      return this.watches.keySet();
   }

   public Color getWatchColor(int id) {
      if (watches.containsKey(id)) {
         return watches.get(id);
      } else {
         return Color.WHITE;
      }
   }

   protected Color getNextWatchColor() {
      Color c = WATCH_COLORS[wcindex];
      wcindex = (wcindex + 1) % WATCH_COLORS.length;
      return c;
   }

   public World getWorld() {
      return workspace.world();
   }

   public int getMaxPX() {
      return workspace.world.maxPxcor();
   }

   public int getMaxPY() {
      return workspace.world.maxPycor();
   }

   public int getMinPX() {
      return workspace.world.minPxcor();
   }

   public int getMinPY() {
      return workspace.world.minPycor();
   }

   public int getWorldWidth() {
      return workspace.world.worldWidth();
   }

   public int getWorldHeight() {
      return workspace.world.worldHeight();
   }

   private void initSlider(BufferedReader in) throws IOException {
      String x0 = in.readLine();
      String y0 = in.readLine();
      String x1 = in.readLine();
      String y1 = in.readLine();
      String name = in.readLine();
      in.readLine();
      String min = in.readLine();
      String max = in.readLine();
      String curr = in.readLine();
      String incr = in.readLine();
      in.readLine();
      String unit = in.readLine();
      if ("nil".equalsIgnoreCase(unit)) unit = "";
      String orientation = in.readLine();
      
      SurfaceSlider slider = new SurfaceSlider(name);
      slider.setMaxValue(toInt(max));
      slider.setMinValue(toInt(min));
      slider.setValue(toInt(curr));
      slider.setHeight(50);
      slider.setWidth(300);
      slider.setPosition(toInt(x0) * 2, toInt(y0) * 2);
      slider.setUnit(unit);
      slider.setButtonListener(this);
      app.addModelWidget(slider);
      
   }

   private void initSwitch(BufferedReader in) throws IOException {
      String x0 = in.readLine();
      String y0 = in.readLine();
      in.readLine();
      in.readLine();
      String name = in.readLine();
      in.readLine();
      boolean checked = "0".equals(in.readLine());

      SurfaceCheckbox box = new SurfaceCheckbox(name);
      box.setChecked(checked);
      box.setPosition(toInt(x0) * 2, toInt(y0) * 2);
      box.setButtonListener(this);
      app.addModelWidget(box);
   }

   private void initPlot(BufferedReader in) throws IOException {
      String x0 = in.readLine();
      String y0 = in.readLine();
      in.readLine(); // width
      in.readLine(); // height
      String title = in.readLine();
      
      org.nlogo.plot.Plot nplot = 
      workspace.plotManager().getPlot(title);
      Plot plot = new Plot(nplot);
         
      plot.setPosition(toInt(x0) * 2, toInt(y0) * 2);
      plot.setXLabel(in.readLine());
      plot.setYLabel(in.readLine());
      plot.setMinX(toFloat(in.readLine()));
      plot.setMaxX(toFloat(in.readLine()));
      plot.setMinY(toFloat(in.readLine()));
      plot.setMaxY(toFloat(in.readLine()));
      in.readLine();  // autoplot
      in.readLine();  // show legend
      in.readLine();  // PENS

      String spec = in.readLine();
      while (spec.length() > 0) {
         //plot.addPen(spec);
         spec = in.readLine();
      }

      app.addModelPlot(plot);
   }
   
   public void buttonPressed(SurfaceWidget button) {
   }

   public void buttonReleased(SurfaceWidget button) {
      try { 
         if (button instanceof SurfaceCheckbox) {
            boolean checked = ((SurfaceCheckbox)button).isChecked();
            setParameter(button.getText(), checked);
         }
         else if (button instanceof SurfaceSlider) {
            int val = ((SurfaceSlider)button).getValue();
            setParameter(button.getText(), val);
         }
      } catch (Exception x) {
         x.printStackTrace();
      }
   }

   private int toInt(String s) {
      try {
         float f = Float.parseFloat(s);
         return (int)f;
      } catch (Exception x) {
         return 0;
      }
   }

      private float toFloat(String s) {
         try {
            return Float.parseFloat(s);
         } catch (Exception x) {
            return 0;
         }
      }
}
 
