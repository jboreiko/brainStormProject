package boardnodes;

import java.awt.Rectangle;

import javax.swing.undo.UndoableEdit;


public class StyledNodeEdit {
	private Object content;
	private StyledNodeEditType type;
	//the added edit
	public StyledNodeEdit(UndoableEdit e) {
		content = e;
		type = StyledNodeEditType.TEXT;
	}
	//@param r		the old location of this node
	public StyledNodeEdit(Rectangle r) {
		content = r;
		type = StyledNodeEditType.DRAG;
	}
	
	public Object getContent() {
		return content;
	}
	
	public StyledNodeEditType getType() {
		return type;
	}
}
