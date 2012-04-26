package boardnodes;
import java.awt.*;

import whiteboard.Whiteboard;
import GUI.WhiteboardPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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

public class StyledNode extends BoardElt implements MouseListener, MouseMotionListener{
	final BoardEltType Type = BoardEltType.NODE;
	public Point startPt,nextPt;
	JTextPane content;
	StyledDocument text;
	WhiteboardPanel _wbp;
	boolean _resizeLock;
	
	public StyledNode(int UID, whiteboard.Whiteboard w,WhiteboardPanel wbp){
		super(UID, w,wbp);
		_resizeLock = false;
		_wbp = wbp;
		content = createEditorPane();
		JScrollPane view = 
			new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		view.setPreferredSize(new Dimension(200,150));
		System.out.println("Y is : " + view.getHeight() + "  X is : " + view.getWidth());
		this.add(view);
		this.setSize(new Dimension(215,165));
		addMouseListener(this);
		addMouseMotionListener(this);
		repaint();
		view.repaint();
		revalidate();
		view.revalidate();
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
	
	/*
	//This exists just to let me peek at progress incrementally
	public static void main(String[] args){
		JFrame node = new JFrame("Text Node Demo");
		StyledNode a = new StyledNode(3, null);
		a.setVisible(true);
		node.add(a);
		node.pack();
		node.setVisible(true);
	}*/


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
	public void setPos(Point p) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("CLICKED!");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("ENTERED!");
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		startPt = new Point(e.getX(),e.getY());
		if(e.getX() > this.getWidth()-15 && e.getY() > this.getHeight()-15){
			_resizeLock = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		_resizeLock = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(_resizeLock){
			System.out.println(startPt);
			System.out.println(e.getX() + "<====X     Y=====>" + e.getY());
			Rectangle currentLocation = getBounds();
			setBounds(currentLocation.x,currentLocation.y,currentLocation.width - (startPt.x - e.getX()),currentLocation.height - (startPt.y - e.getY()));
			startPt.setLocation(e.getX(),e.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
