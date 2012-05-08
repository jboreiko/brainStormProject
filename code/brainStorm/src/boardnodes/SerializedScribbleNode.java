package boardnodes;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SerializedScribbleNode extends SerializedBoardElt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8416218992109699688L;
	public Stack<ScribbleNodeEdit> undos;
	public Stack<ScribbleNodeEdit> redos;
	public Rectangle bounds;
	public Color background;
	
	public SerializedScribbleNode() {
		type = BoardEltType.SCRIBBLE;
	}
}
