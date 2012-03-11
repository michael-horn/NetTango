/*
 * @(#) SimFrame.java
 */
package ntango;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import org.nlogo.api.World;
import org.nlogo.api.Agent;
import org.nlogo.api.AgentSet;

public class SimFrame {

   protected Map<Integer, Turtle> turtles;
   protected List<Patch> patches;

   public SimFrame() {
      this.turtles = new java.util.HashMap<Integer,Turtle>();
      this.patches = new java.util.ArrayList<Patch>();
   }

   public SimFrame(World world) {
      this();
      AgentSet as = world.turtles();
      for (Agent a : as.agents()) {
         Turtle t = new Turtle((org.nlogo.api.Turtle)a);
         turtles.put(t.getID(), t);
      }
      as = world.patches();
      for (Agent a : as.agents()) {
         patches.add(new Patch((org.nlogo.api.Patch)a));
      }
   }

   public Collection<Turtle> getTurtles() {
      return this.turtles.values();
   }

   public List<Patch> getPatches() {
      return this.patches;
   }

   public boolean hasTurtle(Turtle t) {
      return this.turtles.containsKey(t.getID());
   }
   
   public boolean hasTurtle(int id) {
      return this.turtles.containsKey(id);
   }

   public Turtle getTurtle(int id) {
      if (turtles.containsKey(id)) {
         return turtles.get(id);
      } else {
         return null;
      }
   }

   public void markDeadTurtles(SimFrame last) {
      for (Turtle t : last.getTurtles()) {
         if (!t.isDead() && !hasTurtle(t)) {
            Turtle dt = new Turtle(t);
            dt.setDead(true);
            turtles.put(dt.getID(), dt);
         }
      }
   }
}
