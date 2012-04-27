package boardnodes;

import java.awt.Color;
import java.awt.Point;

class ColoredPoint extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6627884708785373974L;
	public Color c;
	public ColoredPoint(int x, int y, Color c) {
		super(x,y);
		this.c = c;
	}
	public ColoredPoint(Point p, Color c) {
		super(p.x, p.y);
		this.c = c;
	}
}