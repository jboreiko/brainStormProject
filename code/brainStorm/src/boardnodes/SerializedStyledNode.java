package boardnodes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Stack;

import javax.swing.text.BadLocationException;

import boardnodes.StyledNode.StyledNodeEdit;

public class SerializedStyledNode extends SerializedBoardElt {

    /**
     * 
     */
    private static final long serialVersionUID = 3919798630752945853L;
    public Stack<StyledNodeEdit> undos;
    public Stack<StyledNodeEdit> redos;
    public Rectangle bounds;
    public String text;
    public Font style;
    public Color fontColor;
    public SerializedStyledNode() {
    	type = BoardEltType.STYLED;
    }
}
