package boardnodes;
import java.awt.*;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoableEdit;

import whiteboard.BoardActionType;

import boardnodes.BoardEltType;

public class StyledNode extends BoardElt {
	final BoardEltType Type = BoardEltType.NODE;
	JTextPane content;
	StyledDocument text;
	
	Stack<UndoableEdit> undos;
	Stack<UndoableEdit> redos;
	
	public StyledNode(int UID, whiteboard.Whiteboard w){
		super(UID, w);
		undos = new Stack<UndoableEdit>();
		redos = new Stack<UndoableEdit>();
		content = createEditorPane();
		JScrollPane view = 
			new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(view);
		this.setSize(new Dimension(215,165));
	}
	
	public class BoardCommUndoableEditListener implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			undos.push(e.getEdit());
			if(e.getEdit().getPresentationName().equals("addition")) {
				try {
					if(text.getText(text.getLength()-1, 1).equals("\n")) {
						text.insertString(text.getLength(), "\u2022 ", null);
					}
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			notifyWhiteboard(BoardActionType.ELT_MOD);
		}
	}
	
	private JTextPane createEditorPane() {
		text = new DefaultStyledDocument();
		text.addUndoableEditListener(new BoardCommUndoableEditListener());
		//text.
		try {
			text.insertString(0, "\u2022 Make a node", null);
			text.insertString(text.getLength(), "\n\u2022 Fill it in", null);
			//if (text.)
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTextPane toReturn = new JTextPane(text);
		toReturn.setPreferredSize(new Dimension(200,150));
		
		return toReturn;
	}
	
	


	@Override
	void decode(String obj) {
	}


	@Override
	public String encode() {
		return null;
	}
	

	@Override
	public void addAction(ActionObject ao) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void redo() {
		if (redos.empty()) 
			return;
		UndoableEdit e = redos.pop();
		if (e.canRedo()) {
			e.redo();
			undos.push(e);
		} else {
			System.out.println("Could not redo " + e);
		}
	}


	@Override
	public void undo() {
		if (undos.empty()) 
			return;
		UndoableEdit e = undos.pop();
		if (e.canUndo()) {
			e.undo();
			redos.push(e);
		} else {
			System.out.println("Could not undo " + e);
		}
	}


	@Override
	public BoardElt clone() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Point getPos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setPos(Point p) {
		// TODO Auto-generated method stub
		
	}

	/*This exists just to let me peek at progress incrementally*/
	public static void main(String[] args){
		JFrame node = new JFrame("Text Node Demo");
		StyledNode a = new StyledNode(3, null);
		a.setVisible(true);
		node.add(a);
		node.pack();
		node.setVisible(true);
	}
}
