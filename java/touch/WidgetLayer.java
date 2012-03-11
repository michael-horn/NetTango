/*
 * @(#) WidgetLayer.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.awt.Graphics2D;
import java.util.List;
import java.util.LinkedList;
import touch.ui.SurfaceWidget;


/**
 * Represents a photoshop like layer of floating widgets.
 * 
 * @author Michael Horn
 * @version $Revision: 1.3 $, $Date: 2008-11-08 01:29:51 $
 */
public class WidgetLayer {

	protected LinkedList<Touchable> widgets;
	protected LinkedList<Touchable> added;

	public WidgetLayer() {
		this.widgets = new LinkedList<Touchable>();
		this.added = new LinkedList<Touchable>();
	}

	public void addWidget(Touchable widget) {
		added.add(widget);
		// widgets.addFirst(widget);
	}

   public void clearWidgets() {
      this.widgets.clear();
      this.added.clear();
   }

	private void bringToTop(Touchable widget) {
		if (widgets.remove(widget)) {
			widgets.addFirst(widget);
		}
	}

	public boolean isTopWidget(Touchable widget) {
		if (widgets.isEmpty()) {
			return false;
		} else {
			return (widget == widgets.getFirst());
		}
	}

	public Touchable getTopWidget() {
		if (widgets.isEmpty()) {
			return null;
		} else {
			return widgets.getFirst();
		}
	}

   public List<Touchable> getWidgets() {
      return widgets;
   }

	public void draw(Graphics2D g) {
		// draw widgets in reverse order
		for (int j = widgets.size() - 1; j >= 0; j--) {
			widgets.get(j).drawWidget(g);
		}
	}

	public void animate() {
		for (Touchable w : widgets) {
			w.animate();
		}
	}

   public void enableWidgets() {
      for (Touchable w : widgets) {
         if (w instanceof SurfaceWidget) {
            ((SurfaceWidget)w).setEnabled(true);
         }
      }
   }

   public void disableWidgets() {
      for (Touchable w : widgets) {
         if (w instanceof SurfaceWidget) {
            ((SurfaceWidget)w).setEnabled(false);
         }
      }
   }

	public void cleanup() {

		// add new widgets
		for (Touchable widget : added) {
			widgets.addFirst(widget);
		}
		added.clear();

		// remove closed widgets
		for (int i = widgets.size() - 1; i >= 0; i--) {
			Touchable widget = widgets.get(i);
			if (widget.isClosed()) {
				widgets.remove(i);
			}
		}
	}

	public void startTouchFrame() {
		for (Touchable widget : widgets) {
			widget.startTouchFrame();
		}
	}

	public Touchable findTouchTarget(TouchEvent e) {
		for (Touchable widget : widgets) {
			if (widget.containsTouch(e)) {
				bringToTop(widget);
				return widget;
			}
		}
		return null;
	}

	public void endTouchFrame() {
		for (Touchable widget : widgets) {
			widget.endTouchFrame();
		}
	}

}
