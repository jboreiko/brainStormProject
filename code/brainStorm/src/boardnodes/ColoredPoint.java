package boardnodes;

import java.awt.Color;
import java.awt.Point;

class ColoredPoint extends Point {
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