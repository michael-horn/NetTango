/*
 * @(#) Palette.java
 */
package ntango;

import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Palette {

	public static Color TRANSPARENT = new Color(1, 1, 1, 0);
	public static Color TRANS_WHITE = new Color(1, 1, 1, 0.75f);
	public static Color TRANS_BLACK = new Color(0, 0, 0, 0.5f);

	public static Font FONT_H1 = new Font(null, Font.BOLD, 18);
	public static Font FONT_H1_ITAL = new Font(null, Font.BOLD + Font.ITALIC, 18);
	public static Font FONT_H2 = new Font(null, Font.BOLD, 16);
	public static Font FONT_BODY = new Font(null, 0, 14);
	public static Font FONT_BODY_BOLD = new Font(null, Font.BOLD, 14);
	public static Font FONT_BODY_ITAL = new Font(null, Font.ITALIC, 14);
	public static Font FONT_SMALL = new Font(null, 0, 12);
	public static Font FONT_SMALL_ITAL = new Font(null, Font.ITALIC, 12);

	public static Stroke STROKE1 = new BasicStroke(1);
	public static Stroke STROKE2 = new BasicStroke(2);
	public static Stroke STROKE3 = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	public static Stroke STROKE4 = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	public static int stringWidth(Graphics2D g, String s) {
		return g.getFontMetrics().stringWidth(s);
	}

	public static void drawUpTriangle(Graphics2D g, int x, int y) {
		GeneralPath tri = new GeneralPath();
		tri.moveTo(x, y + 14);
		tri.lineTo(x + 16, y + 14);
		tri.lineTo(x + 8, y);
		tri.closePath();
		g.fill(tri);
	}

	public static void drawDownTriangle(Graphics2D g, int x, int y) {
		GeneralPath tri = new GeneralPath();
		tri.moveTo(x, y);
		tri.lineTo(x + 16, y);
		tri.lineTo(x + 8, y + 14);
		tri.closePath();
		g.fill(tri);
	}

	public static String truncateString(Graphics2D g, String str, int width) {
		if (str == null)
			return null;
		int fw = stringWidth(g, str);
		if (fw < width)
			return str;

		while (str.length() > 3) {
			str = str.substring(0, str.length() - 1);
			fw = stringWidth(g, str + "...");
			if (fw < width) {
				return str + "...";
			}
		}
		return null;
	}

	public static String formatNumber(int number) {
		String s = String.valueOf(number);
		int digits = s.length();
		for (int i = digits - 3; i > 0; i -= 3) {
			s = s.substring(0, i) + "," + s.substring(i);
		}
		return s;
	}

	/**
	 * Returns an audio clip, or null if the path was invalid.
	 */
	public static AudioClip createAudioClip(String path) {
		java.net.URL audioURL = Palette.class.getResource(path);
		if (audioURL != null) {
			return java.applet.Applet.newAudioClip(audioURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Returns a buffered image from the given path.
	 */
	public static BufferedImage createImage(String path) {
		java.net.URL imageURL = Palette.class.getResource(path);
		try {
			return javax.imageio.ImageIO.read(imageURL);
		} catch (java.io.IOException iox) {
			System.err.println("Unable to read image file: " + path);
			return null;
		}
	}


	public static BufferedImage createCompatibleImage(int w, int h) {
      if (touch.SurfaceFrame.device == null) {
         return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      } else {
         return touch.SurfaceFrame.device.getDefaultConfiguration().createCompatibleImage(w, h);
      }
	}
}
