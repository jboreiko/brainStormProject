package boardnodes;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SerializedStyledNode extends SerializedBoardElt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2508046322485017589L;
	/**
	 * 
	 */
	
	public LinkedList<List<ColoredPoint>> drawnArea;
	public LinkedList<List<ColoredPoint>> undrawnArea;
	public Stack<StyledNodeEdit> undos;
	public Stack<StyledNodeEdit> redos;
	public Rectangle bounds;
	
	public SerializedStyledNode() {
		type = BoardEltType.STYLED;
	}
}
