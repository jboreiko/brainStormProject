package boardnodes;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import whiteboard.BoardActionType;

import boardnodes.BoardEltType;

public class StyledNode extends BoardElt {
	final BoardEltType Type = BoardEltType.NODE;
	JTextPane content;
	StyledDocument text;
	
	public StyledNode(int UID, whiteboard.Whiteboard w){
		super(UID, w);
		content = createEditorPane();
		JScrollPane view = 
			new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(view);
		this.setSize(new Dimension(215,165));
	}
	
	public class BoardCommUndoableEditListener implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
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
			System.out.println("Send to backend "+e.getEdit().getPresentationName());
		}
		
	}
	
	private JTextPane createEditorPane() {
		text = new DefaultStyledDocument();
		text.addUndoableEditListener(new BoardCommUndoableEditListener());
		try {
			text.insertString(0, "\u2022 Make a node", null);
			text.insertString(text.getLength(), "\n\u2022 Fill it in", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTextPane toReturn = new JTextPane(text);
		toReturn.setPreferredSize(new Dimension(200,150));
		
		return toReturn;
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
		// TODO Auto-generated method stub
		
	}


	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
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
	public int getUID() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setPos(Point p) {
		// TODO Auto-generated method stub
		
	}

	
}
