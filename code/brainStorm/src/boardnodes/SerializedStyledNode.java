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
    public String lastText;
    public Font lastFont;
    public SerializedStyledNode(StyledNode sn) {
        type = BoardEltType.STYLED;
        bounds = sn.getBounds();
        UID = sn.UID;
        text = new String(sn.content.getText());
        style = sn.content.getFont();
        fontColor = sn.content.getForeground();
        lastText = sn.lastText;
        lastFont = sn.lastFont;
        undos = sn.undos;
        redos = sn.redos;
    }
}
