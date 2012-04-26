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
	private Point startPt,nextPt;
	JTextPane content;
	StyledDocument text;
	Stack<UndoableEdit> undos;
	Stack<UndoableEdit> redos;
	WhiteboardPanel _wbp;
	JScrollPane view;
	boolean _resizeLock,_dragLock;
	
	public final static int BORDER_WIDTH = 11;
	public final static Dimension DEFAULT_SIZE = new Dimension(200,150);
	
	public StyledNode(int UID, whiteboard.Backend w){
		super(UID, w);
		type = BoardEltType.STYLED;
		setLayout(null);
		undos = new Stack<UndoableEdit>();
		redos = new Stack<UndoableEdit>();
		_resizeLock = false;
		_dragLock = false;
		content = createEditorPane();
		view = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		view.setPreferredSize(DEFAULT_SIZE);
		
		System.out.println("Y is : " + view.getHeight() + "  X is : " + view.getWidth());
		System.out.println("SCrollpane's location in parent is: " + view.getBounds());
		view.setBounds(BORDER_WIDTH, BORDER_WIDTH, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
		System.out.println("SCrollpane's location in parent is: " + view.getBounds());
		this.add(view);
		this.setSize(new Dimension(DEFAULT_SIZE.width + BORDER_WIDTH*2, DEFAULT_SIZE.height + BORDER_WIDTH*2));
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
			notifyBackend(BoardActionType.ELT_MOD);
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
		if (e.getX() < BORDER_WIDTH && e.getY() < BORDER_WIDTH) {
			System.out.println("Deleting " + this.getUID());
			backend.remove(this.getUID());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("ENTERED");
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
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
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
		System.out.println(e.getPoint());
		int dx = e.getX() - startPt.x;
		int dy = e.getY() - startPt.y;
		int screenX = e.getX() + getBounds().x;
		int screenY = e.getY() + getBounds().y;
		System.out.println("screenX,Y: " + screenX+" " + screenY);
		Rectangle previousBounds = getBounds();
		Rectangle prevView = view.getBounds();
		if(_resizeLock){
			if (e.getX() > BORDER_WIDTH*8) { //the resize leaves us with positive width
				setBounds(previousBounds.x, previousBounds.y, e.getX(), previousBounds.height);
				view.setBounds(prevView.x, prevView.y, e.getX()-BORDER_WIDTH*2, prevView.height);
			}
			if (e.getY()> BORDER_WIDTH*8) { //the resize leaves us with positive height
				setBounds(previousBounds.x, previousBounds.y, getBounds().width, e.getY());
				view.setBounds(prevView.x, prevView.y, view.getBounds().width, e.getY()-BORDER_WIDTH*2);
			}
			view.setPreferredSize(new Dimension(view.getBounds().width, view.getBounds().height));
			startPt.setLocation(e.getX(), e.getY());
		} else if (_dragLock) {

			if (previousBounds.x + dx >= 0 && previousBounds.y + dy >= 0)
				setBounds(previousBounds.x + dx, previousBounds.y + dy, previousBounds.width, previousBounds.height);
			//if (screenX>= 0 && screenY>= 0)
			//	setBounds(screenX, screenY, previousBounds.width, previousBounds.height);
		}
		/*else if(_dragLock){
			System.out.println(startPt);
			System.out.println(e.getX() + "<=====X     Y=====>" + e.getY());
			setBounds(nodeBounds.x + xoffset,nodeBounds.y + yoffset,nodeBounds.width,nodeBounds.height);
			//view.setBounds(viewBounds.x + xoffset,viewBounds.y + yoffset,viewBounds.width,viewBounds.height);
		}*/
		wbp.extendPanel(getBounds());
		repaint();
		revalidate();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
			this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}
		else if(e.getX() > this.getWidth()-BORDER_WIDTH|| e.getX() < BORDER_WIDTH || e.getY() < BORDER_WIDTH|| e.getY() > this.getHeight()-BORDER_WIDTH) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(Color.RED);
		g.fillRect(0, 0, BORDER_WIDTH, BORDER_WIDTH);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(getWidth()-BORDER_WIDTH, getHeight()-BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
	}
	
}
