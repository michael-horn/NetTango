/*
 * @(#) TweenManager.java
 */
package tween;

import java.util.List;

public class TweenManager {

   protected List<Tween> list;
   protected boolean running = true;

   public TweenManager() {
      this.list = new java.util.ArrayList<Tween>();
   }

   public void pause() {
      this.running = false;
   }

   public void play() {
      this.running = true;
   }

   public void add(Tween t) {
      this.list.add(t);
   }

   public void remove(String property) {
      if (property == null) return;
      for (int i=list.size() - 1; i >= 0; i--) {
         Tween t = list.get(i);
         if (property.equals(t.getProperty())) {
            list.remove(t);
         }
      }
   }

   public void animate() {
      if (running) {
         for (int i=list.size() - 1; i >= 0; i--) {
            Tween t = list.get(i);
            t.animate();
            if (!t.isTweening()) {
               list.remove(t);
            }
         }
      }
   }
}