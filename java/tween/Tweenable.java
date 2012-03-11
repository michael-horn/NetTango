/*
 * @(#) Tweenable.java
 */
package tween;

public interface Tweenable {

   public void startTween(String property, Tween tween);
   public void endTween(String property, Tween tween);
   public void setTweenValue(String property, Tween tween);
}