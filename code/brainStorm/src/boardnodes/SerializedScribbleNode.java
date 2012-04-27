package boardnodes;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import boardnodes.ScribbleNode.ScribbleNodeEditType;
import boardnodes.ScribbleNode.ScribbleNodeEdit;

public class SerializedScribbleNode extends SerializedBoardElt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8416218992109699688L;
	public LinkedList<List<ColoredPoint>> drawnArea;
	public LinkedList<List<ColoredPoint>> undrawnArea;
	Stack<ScribbleNodeEdit> undos;
	Stack<ScribbleNodeEdit> redos;
	public int x;
	public int y;
	
	public SerializedScribbleNode() {
		type = BoardEltType.SCRIBBLE;
	}
}
