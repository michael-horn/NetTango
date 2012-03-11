/*
 * @(#) Tween.java
 */
package tween;


public abstract class Tween<T> {

   public static final int TWEEN_LINEAR = 0;
   public static final int TWEEN_SINE2  = 1;
   public static final int TWEEN_DECAY  = 2;
   public static final int TWEEN_LOG    = 3;

   public static final int REPEAT_FOREVER = -1;

   private T start;      // start value
   private T end;        // end value
   private int delay;    // delay ticks
   private int duration; // tween duration (ticks)
   private int count;    // current tick count
   private int func;     // interpolation function
   private int repeat;   // repeat count
   private boolean running; // is paused or not
   
   private Tweenable target;
   private String pname;

   
   public Tween() {
      this.start    = null;
      this.end      = null;
      this.delay    = 0;
      this.duration = 0;
      this.count    = 0;
      this.func     = TWEEN_LINEAR;
      this.repeat   = 1;
      this.target   = null;
      this.pname    = null;
      this.running  = true;
   }

   public void pause() {
      this.running = false;
   }

   public void play() {
      this.running = true;
   }
   
   public void stop() {
      this.repeat = 0;
      this.running = false;
      this.count = 1;
   }

   public boolean isRunning() {
      return this.running;
   }
   
   public void restart() {
      this.count = 0;
      this.running = true;
   }

   public void setTarget(Tweenable target, String property) {
      this.target = target;
      this.pname = property;
   }

   public String getProperty() {
      return this.pname;
   }

   public boolean isTweening() {
      if (repeat == REPEAT_FOREVER) {
         return true;
      } else {
         return count <= (duration * repeat) + delay;
      }
   }

   public void fastForward(int ticks) {
      count += ticks;
   }

   public void fastForwardSeconds(float s) {
      count += (int)(s * 33.3f);
   }

   public boolean isDelaying() {
      return count < delay;
   }

   public int getDelay() {
      return this.delay;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }

   public void setDelaySeconds(float s) {
      this.delay = (int)(s * 33.3f);
   }

   public abstract T getValue();

   protected float getTime() {
      float t = (float)(count - delay) / (float)duration;
      if (t < 0) {
         return 0;
      } else if (t > 1 && repeat != 1) {
         return t - (float)Math.floor(t);
      } else {
         return t;
      }
   }

   protected float getY() {
      float time = getTime();
      double x;
      
      switch (func) {
      case TWEEN_LINEAR:
         return time;

      case TWEEN_SINE2:
         x = time * Math.PI * 0.5;
         return (float)(Math.sin(x) * Math.sin(x));

      case TWEEN_LOG:
         x = time * (Math.E - 1);
         return (float)Math.log1p(x);

      case TWEEN_DECAY:
         return 1 - (float)Math.exp(time * -5);
         
      default:
         return time;
      }
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public void setDurationSeconds(float s) {
      this.duration = (int)(33.3f * s);
   }

   public int getFunction() {
      return this.func;
   }

   public void setFunction(int function) {
      this.func = function;
   }

   public T getStart() {
      return this.start;
   }

   public void setStart(T start) {
      this.start = start;
   }
   
   public T getEnd() {
      return this.end;
   }

   public void setEnd(T end) {
      this.end = end;
   }

   public int getRepeat() {
      return this.repeat;
   }

   public void setRepeat(int repeat) {
      this.repeat = repeat;
   }

   public void animate() {
      if (!running) return;

      if (count == delay) {
         if (target != null && pname != null) {
            target.startTween(pname, this);
         }
      }
      
      if (isTweening()) {
         count++;
         if (!isTweening()) {
            if (target != null && pname != null) {
               target.endTween(pname, this);
            }
         }
      }

      if (isTweening() && !isDelaying()) {
         if (target != null && pname != null) {
            target.setTweenValue(pname, this);
         }
      }
   }
}
