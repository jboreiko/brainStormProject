package whiteboard;

import java.awt.Point;

public abstract class BoardElt {
	protected static int nextUID = 0;
	
	public abstract int getUID();
	public abstract BoardElt clone();
	public abstract void setPos(Point p);
	public abstract String getText();
	public abstract String setText(String s);

	public abstract Object getType();

	public abstract Object getPos();
	public abstract String encode();
	
	public static BoardElt decode(String s) {
		//TODO: implement
		return null;
	}
	public int containsText(String q) {
		return getText().toLowerCase().indexOf(q.toLowerCase());
	}
}
