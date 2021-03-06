package boardnodes;

import java.awt.Point;
import java.util.Stack;

public class SerializedBoardPath extends SerializedBoardElt {
    private static final long serialVersionUID = -4310196162247409269L;

    public BoardPathType _stroke; 
    public int _snapSeminal;
    public int _snapTerminal;
    public Point _seminal;
    public Point _terminal;
    public Stack<ActionObject> pastPositions;
    public Stack<ActionObject> futurePositions;

	public Point _snapSeminalOffset;
	public Point _snapTerminalOffset;
    
    public SerializedBoardPath() {
        type = BoardEltType.PATH;
    }
}
