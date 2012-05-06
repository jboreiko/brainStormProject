package boardnodes;

import java.awt.Color;
import java.awt.Point;

class ColoredPoint extends Point {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6627884708785373974L;
	public Color c;
	public int s;
	public ColoredPoint(int x, int y, Color c,int s) {
		super(x,y);
		this.c = c;
		this.s = s;
	}
	public ColoredPoint(Point p, Color c,int s) {
		super(p.x, p.y);
		this.c = c;
		this.s = s;
	}
}