package com.thatgamerblue.osbot.util;

import org.osbot.rs07.api.map.Position;

public class CArea extends org.osbot.rs07.api.map.Area {

	public CArea(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}
	
	public CArea(int[][] k) {
		super(k);
	}
	
	public CArea(Position x, Position x1) {
		super(x, x1);
	}
	
	public CArea(Position[] x) {
		super(x);
	}
	
	public String toString() {
		String s = "";
		for (Position p : getPositions()) {
			s += p.getX() + " " + p.getY() + " : ";
		}
		s = s.substring(0, s.length() - 3);
		return s;
	}
	
	public static CArea fromOSBotArea(org.osbot.rs07.api.map.Area area) {
		return new CArea(area.getPositions().toArray(new Position[0]));
	}
	
	public CArea setPlane(int plane) {
		return fromOSBotArea(super.setPlane(plane));
	}

}
