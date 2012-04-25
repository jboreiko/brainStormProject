package whiteboard;

import java.awt.Point;
import java.awt.geom.Point2D;
import boardnodes.BoardEltType;

public abstract class BoardElt {
	protected static int nextUID = 0;
	
	private BoardEltType type;
	public abstract int getUID();
	public abstract BoardElt clone();
	public abstract void setPos(Point p);
	public abstract String getText();
	public abstract String setText(String s);

	public BoardEltType getType() {
		return type;
	}

	public abstract Point2D getPos();
	
	
	public int containsText(String q) {
		return getText().toLowerCase().indexOf(q.toLowerCase());
	}
}
