package boardnodes;

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
    public SerializedStyledNode(StyledNode sn) {
        type = BoardEltType.STYLED;
        bounds = sn.getBounds();
        undos = sn.undos;
        redos = sn.redos;
        UID = sn.UID;
        try {
            text = sn.text.getText(0, sn.text.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
