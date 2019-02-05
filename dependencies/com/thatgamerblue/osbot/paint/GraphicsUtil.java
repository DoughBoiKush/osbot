package com.thatgamerblue.osbot.paint;

import org.osbot.rs07.script.Script;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.LinkedList;

public class GraphicsUtil {
	
	public Graphics2D g;
	
	MousePainter mousePainter;
	
	public GraphicsUtil(Graphics2D g) {
		this.g = g;
		mousePainter = new MousePainter(g);
	}
	
	public void drawShadowedString(String s, int x, int y, Color textColor) {
		drawShadowedString(s, x, y, 3, 3, textColor, Color.BLACK);
	}
	
	public void drawShadowedString(String s, int x, int y, int shadowOffsetX, int shadowOffsetY, Color textColor) {
		drawShadowedString(s, x, y, shadowOffsetX, shadowOffsetY, textColor, Color.BLACK);
	}
	
	public void drawShadowedString(String s, int x, int y, Color textColor, Color shadowColor) {
		drawShadowedString(s, x, y, 3, 3, textColor, shadowColor);
	}
	
	public void drawShadowedString(String s, int x, int y, int shadowOffsetX, int shadowOffsetY, Color textColor, Color shadowColor) {
		Color c = g.getColor();
		
		g.setColor(shadowColor);
		g.drawString(s, x + shadowOffsetX, y + shadowOffsetY);
		g.setColor(textColor);
		g.drawString(s, x, y);
		
		g.setColor(c);
	}
	
	public void paintMouse(Script script, boolean lenny) {
		mousePainter.paintMouse(script, lenny);
	}
	
	private class MousePainter {
		private int mX, mY;
		private long angle;
		private BasicStroke cursorStroke = new BasicStroke(2);
		private Color cursorColor = Color.WHITE;
		private AffineTransform oldTransform;
		private LinkedList<MousePathPoint> mousePath = new LinkedList<>();
		Graphics2D g;
		
		public MousePainter(Graphics2D gr) {
			g = gr;
		}
		
		public void paintMouse(Script client, boolean lenny) {
			Color col = g.getColor();
			oldTransform = g.getTransform();
			mX = client.getMouse().getPosition().x;
			mY = client.getMouse().getPosition().y;
			
			g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
			
			//MOUSE TRAIL
			while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
				mousePath.remove();
			}
			
			MousePathPoint mpp = new MousePathPoint(mX, mY, 300);
			
			if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp) || mX == -1) {
				mousePath.add(mpp);
			}
			
			MousePathPoint lastPoint = null;
			for (MousePathPoint a : mousePath) {
				if (lastPoint != null) {
					g.setColor(new Color(255, 255, 255, a.getAlpha()));    //trail color
					g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
				}
				lastPoint = a;
			}
			
			if (lenny) {
				g.setColor(cursorColor);
				g.drawString("( ͡° ͜ʖ ͡°)", mX - 2, mY + 10);
				return;
			}
			
			if (mX != -1) {
				g.setStroke(cursorStroke);
				g.setColor(cursorColor);
				g.drawLine(mX - 3, mY - 3, mX + 2, mY + 2);
				g.drawLine(mX - 3, mY + 2, mX + 2, mY - 3);
				
				g.rotate(Math.toRadians(angle += 6), mX, mY);
				
				g.draw(new Arc2D.Double(mX - 12, mY - 12, 24, 24, 330, 60, Arc2D.OPEN));
				g.draw(new Arc2D.Double(mX - 12, mY - 12, 24, 24, 151, 60, Arc2D.OPEN));
				
				g.setTransform(oldTransform);
			}
			g.setColor(col);
		}
	}
	
}