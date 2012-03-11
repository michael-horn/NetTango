/*
 * @(#) SimStream.java
 */
package ntango;


public class SimStream {

   protected SimFrame[] frames;

   // frame count
   protected int count;

   // index of the first frame in the buffer (may not be 0)
   protected int first;

   
   public SimStream(int capacity) {
      this.frames = new SimFrame[capacity];
      this.count = 0;
      this.first = 0;
   }

   public int getFrameCount() {
      return count;
   }

   public int getCapacity() {
      return frames.length;
   }

   public boolean isBufferFull() {
      return getFrameCount() >= getCapacity();
   }

   public int getMinIndex() {
      return first;
   }

   public int getMaxIndex() {
      return first + count - 1;
   }

   public void clear() {
      this.first = 0;
      this.count = 0;
   }

   public void addFrame(SimFrame frame) {
      int index = (getMaxIndex() + 1) % getCapacity();
      frames[index] = frame;
      if (isBufferFull()) {
         first++;
      } else {
         count++;
      }
   }

   public SimFrame getLastFrame() {
      return getFrame(getMaxIndex());
   }

   public SimFrame getFrame(int i) {
      if (i >= getMinIndex() && i <= getMaxIndex()) {
         return frames[ (i % getCapacity()) ];
      } else {
         return null;
      }
   }
}

