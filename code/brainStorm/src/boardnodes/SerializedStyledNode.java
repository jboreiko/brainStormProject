package boardnodes;

import java.awt.Rectangle;
import javax.swing.text.BadLocationException;

public class SerializedStyledNode extends SerializedBoardElt {

    /**
     * 
     */
    private static final long serialVersionUID = 3919798630752945853L;
    public Rectangle bounds;
    public String text;
    public SerializedStyledNode(StyledNode sn) {
        type = BoardEltType.STYLED;
        bounds = sn.getBounds();
        try {
            text = sn.text.getText(0, sn.text.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
