/*
 * @(#) SurfaceConnector.java
 * 
 * NetLogo Jr.
 * Learning Sciences, School of Education and Social Policy
 * Northwestern University
 * 
 * Copyright (c) 2010, Northwestern University
 */
package touch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.Timer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SurfaceConnector extends DefaultHandler
   implements ActionListener, Runnable, MouseListener, MouseMotionListener {

   protected Timer animator;

   protected SAXParser parser;

   protected TouchDelegator delegator;

   protected TouchListener mouse_target = null;

   protected List<TouchEvent> prev;
   protected List<TouchEvent> curr;
   protected List<TouchEvent> temp;
   
   protected String host;
   
   protected int port;

   
	public SurfaceConnector(TouchDelegator delegator, String host, int port) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);

			this.delegator = delegator;
			this.host = host;
			this.port = port;
			this.parser = factory.newSAXParser();
			this.animator = new Timer(33, this);
			this.curr = null;
			this.prev = null;
			this.temp = null;
		} catch (Exception pcx) {
			pcx.printStackTrace();
		}
	}

	public void start() {
		animator.start();
		(new Thread(this)).start();
	}

	public void stop() {
		animator.stop();
	}


	public void run() {
		Socket socket = new Socket();
		try {
			InetSocketAddress inet = new InetSocketAddress(host, port);
			socket.connect(inet, 500);
			parser.reset();
			parser.parse(new XMLStream(socket.getInputStream()), this);
		} catch (IOException iox) {
         System.out.println("No surface connection.");
		} catch (SAXException saxx) {
			saxx.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception x) {
				;
			}
		}
	}

	public void startDocument() {
		System.out.println("connected to surface event stream");
	}

	public void endDocument() {
		System.out.println("connection terminated");
	}

	public void startElement(String uri, String lname, String qname, Attributes attr) {
		if ("frame".equals(qname)) {
			this.temp = new java.util.ArrayList<TouchEvent>();
		}

		else if ("contact".equals(qname)) {
			TouchEvent c = new TouchEvent(attr);
			if (temp != null) temp.add(c);
		}
	}

	public void endElement(String uri, String lame, String qname) {
		if ("frame".equals(qname)) {
			// only overwrite curr if the animation thread is ready
			if (this.curr == null) {
				this.curr = temp;
				this.temp = null;
			}
		} else if ("session".equals(qname)) {
			delegator.closeApplication();
		}
	}

	protected void processFrame() {
		if (delegator == null) return;
		if (curr == null)	{
         delegator.startTouchFrame(0);
         delegator.endTouchFrame();
         return;
      }

		delegator.startTouchFrame(curr.size());

		for (TouchEvent te : curr) {
			TouchEvent pe = getPreviousTouch(te);
			te.setPrevious(pe);

         if (pe != null && pe.hasTarget()) {
            if (pe.getTarget().isClosed()) {
               pe = null;
            }
         }

			if (pe != null) {
				te.setTarget(pe.getTarget());
				te.setTouchTime(pe.getTouchTime());
				if (te.hasTarget()) {
					te.getTarget().addTouchEvent(te);
				}
			} else {
				Touchable target = delegator.findTouchTarget(te);
				if (target != null) {
					te.setTarget(target);
					target.addTouchEvent(te);
				}
			}
		}

		delegator.endTouchFrame();
		prev = curr;

		// setting curr to null signals event thread that we can
		// accept a new frame
		this.curr = null;
	}

	protected TouchEvent getPreviousTouch(TouchEvent e) {
		if (prev != null) {
			for (TouchEvent t : prev) {
				if (t.getTouchID() == e.getTouchID()) {
					return t;
				}
			}
		}
		return null;
	}

	/******************************************************************/
	/*                         MOUSE EVENTS                           */
	/******************************************************************/
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
      if (delegator != null) {
         TouchEvent touch = new TouchEvent(e);
         Touchable target = delegator.findTouchTarget(touch);
         if (target != null) {
            mouse_target = target.getListener();
            mouse_target.mouseMoved(e);
         } else {
            mouse_target = null;
         }
      }
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
      if (delegator != null) {
         TouchEvent touch = new TouchEvent(e);
         Touchable target = delegator.findTouchTarget(touch);
         if (target != null) {
            mouse_target = target.getListener();
            mouse_target.mousePressed(e);
         } else {
            mouse_target = null;
         }
      }
	}

	public void mouseReleased(MouseEvent e) {
      if (delegator != null) {
         if (mouse_target != null) {
            mouse_target.mouseReleased(e);
            mouse_target = null;
         }
      }
	}

	public void mouseDragged(MouseEvent e) {
      if (delegator != null) {
         if (mouse_target != null) {
            mouse_target.mouseDragged(e);
         }
      }
	}

	public void actionPerformed(ActionEvent e) {
		processFrame();
	}


	class XMLStream extends java.io.FilterInputStream {

		public XMLStream(java.io.InputStream src) {
			super(
				new java.io.SequenceInputStream(
					new java.io.ByteArrayInputStream(
						"<session>".getBytes()), src));
		}

		public int read(byte [] b, int off, int len) throws IOException {
			int count = super.read(b, off, len);
			if (count >= 0) {
				for (int i=off; i<count; i++) {
					if (i < b.length) {
						if (b[i] == '\0') b[i] = ' ';
					}
				}
			}
			return count;
		}
			
		public int read() throws IOException {
			int c = super.read();
			return (c == '\0') ? ' ' : c;
		}
	}
}
