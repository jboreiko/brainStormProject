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
}
