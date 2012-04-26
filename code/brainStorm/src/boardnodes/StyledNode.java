package boardnodes;
import java.awt.*;
import java.util.Stack;
import whiteboard.Backend;
import GUI.WhiteboardPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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

public class StyledNode extends BoardElt implements MouseListener, MouseMotionListener{
	public static int UIDCounter = 0;
	final BoardEltType Type = BoardEltType.NODE;
	private Point startPt,nextPt;
	JTextPane content;
	StyledDocument text;
	Stack<UndoableEdit> undos;
	Stack<UndoableEdit> redos;
	WhiteboardPanel _wbp;
	JScrollPane view;
	boolean _resizeLock,_dragLock;
	
	public StyledNode(int UID, whiteboard.Backend w){
		super(UID, w);
		undos = new Stack<UndoableEdit>();
		redos = new Stack<UndoableEdit>();
		_resizeLock = false;
		_dragLock = false;
		content = createEditorPane();
		view = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		view.setPreferredSize(new Dimension(200,150));
		
		System.out.println("Y is : " + view.getHeight() + "  X is : " + view.getWidth());
		this.add(view);
		this.setSize(new Dimension(215,165));
		addMouseListener(this);
		addMouseMotionListener(this);
		
		revalidate();
		view.revalidate();
		repaint();
		view.repaint();
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
		//text.
		try {
			text.insertString(0, "\u2022 Make a node", null);
			text.insertString(text.getLength(), "\n\u2022 Fill it in", null);
			//if (text.)
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text.addUndoableEditListener(new BoardCommUndoableEditListener());
		JTextPane toReturn = new JTextPane(text);
		
		return toReturn;
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
		wbp.setListFront(this);
		startPt = new Point(e.getX(),e.getY());
		if(e.getX() > this.getWidth()-15 && e.getY() > this.getHeight()-15){
			_resizeLock = true;
		}
		else {
			_dragLock = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		_resizeLock = false;
		_dragLock = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Rectangle nodeBounds = getBounds();
		Rectangle viewBounds = view.getBounds();
		int xoffset =  - (startPt.x - e.getX());
		int yoffset =  - (startPt.y - e.getY());
		if(_resizeLock){
			if(nodeBounds.width + xoffset < 30 && nodeBounds.height + yoffset < 30){
				xoffset = 0;
				yoffset = 0;
			}
			else if(nodeBounds.width + xoffset < 30){
				xoffset = 0;
			}
			else if(nodeBounds.height + yoffset < 30){
				yoffset = 0;
			}
				System.out.println(startPt);
				System.out.println(e.getX() + "<=====X     Y=====>" + e.getY());
				setBounds(nodeBounds.x,nodeBounds.y,nodeBounds.width + xoffset,nodeBounds.height + yoffset);
				view.setBounds(viewBounds.x,viewBounds.y,viewBounds.width + xoffset,viewBounds.height + yoffset);
				view.setPreferredSize(new Dimension(viewBounds.width + xoffset,viewBounds.height + yoffset));
				repaint();
				revalidate();
				startPt.setLocation(e.getX(),e.getY());
		}
		else if(_dragLock){
			System.out.println(startPt);
			System.out.println(e.getX() + "<=====X     Y=====>" + e.getY());
			setBounds(nodeBounds.x + xoffset,nodeBounds.y + yoffset,nodeBounds.width,nodeBounds.height);
			view.setBounds(viewBounds.x + xoffset,viewBounds.y + yoffset,viewBounds.width,viewBounds.height);
			repaint();
			revalidate();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
