package boardnodes;
import java.awt.*;
import java.util.Stack;
import whiteboard.Backend;
import GUI.ViewportDragScrollListener;
import GUI.WhiteboardPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

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

public class StyledNode extends BoardElt implements MouseListener, MouseMotionListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -161595885786250168L;
	public static int UIDCounter = 0;
	private Point startPt,nextPt;
	JTextPane content;
	StyledDocument text;
	Stack<StyledNodeEdit> undos;
	Stack<StyledNodeEdit> redos;
	WhiteboardPanel _wbp;
	JScrollPane view;
	boolean _resizeLock,_dragLock;

	public final static int BORDER_WIDTH = 10;
	public final static Dimension DEFAULT_SIZE = new Dimension(200,150);

	public StyledNode(int UID, whiteboard.Backend w){
		super(UID, w);
		type = BoardEltType.STYLED;
		setLayout(null);
		undos = new Stack<StyledNodeEdit>();
		redos = new Stack<StyledNodeEdit>();
		_resizeLock = false;
		_dragLock = false;
		content = createEditorPane();
		view = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		view.setBounds(BORDER_WIDTH, BORDER_WIDTH, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
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
			undos.push(new StyledNodeEdit(e.getEdit()));
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
		try {
			text.insertString(0, "\u2022 Make a node", null);
			text.insertString(text.getLength(), "\n\u2022 Fill it in", null);
		} catch (BadLocationException e) {
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
		StyledNodeEdit f = redos.pop();
		if (f.type == StyledNodeEditType.TEXT) {
			UndoableEdit e = (UndoableEdit) f.content;
			if (e.canRedo()) {
				e.redo();
				undos.push(f);
			} else {
				System.out.println("Could not redo " + e);
			}
		} else if (f.type == StyledNodeEditType.DRAG) {
			Rectangle r = (Rectangle) f.content;
			undos.push(new StyledNodeEdit(new Rectangle(getBounds())));
			setBounds(r);
			view.setBounds(BORDER_WIDTH, BORDER_WIDTH, r.width-2*BORDER_WIDTH, r.height-2*BORDER_WIDTH);
		}
		revalidate();
		repaint();
	}

	@Override
	public void undo() {
		if (undos.empty()) 
			return;
		StyledNodeEdit f = undos.pop();
		if (f.type == StyledNodeEditType.TEXT) {
			UndoableEdit e = (UndoableEdit) f.content;
			if (e.canUndo()) {
				e.undo();
				redos.push(f);
			} else {
				System.out.println("Could not undo " + e);
			}
		} else if (f.type == StyledNodeEditType.DRAG) {
			Rectangle r = (Rectangle) f.content;
			redos.push(new StyledNodeEdit(new Rectangle(getBounds())));
			setBounds(r);
			view.setBounds(BORDER_WIDTH, BORDER_WIDTH, r.width-2*BORDER_WIDTH, r.height-2*BORDER_WIDTH);
		}
		revalidate();
		repaint();
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
			backend.remove(this.getUID());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if(_mouseListener!=null) {
			System.out.println("asdfa");
			if(_mouseListener.draggedPath!=null) {
				System.out.println("whoo");
				_mouseListener.draggedPath._snapSeminal = this;
			}
		}
		System.out.println("ENTERED");
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if(_mouseListener!=null) {
			System.out.println("asdfa");
			if(_mouseListener.draggedPath!=null) {
				System.out.println("whoo");
				_mouseListener.draggedPath._snapSeminal = null;
			}
		}
	}

	Rectangle boundsBeforeMove;
	public void mousePressed(MouseEvent e) {
		wbp.setListFront(this);
		startPt = new Point(e.getX(),e.getY());
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
			_resizeLock = true;
		}
		else {
			_dragLock = true;
		}
		boundsBeforeMove = getBounds();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (_resizeLock || _dragLock) {
			System.out.println("releasing lock " + _resizeLock + _dragLock);
			undos.push(new StyledNodeEdit(boundsBeforeMove));
			notifyBackend(BoardActionType.ELT_MOD);
			System.out.println(undos.size());
		}
		_resizeLock = false;
		_dragLock = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dx = e.getX() - startPt.x;
		int dy = e.getY() - startPt.y;
		int screenX = e.getX() + getBounds().x;
		int screenY = e.getY() + getBounds().y;
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

			if (previousBounds.x + dx >= 0 && previousBounds.y + dy >= 0) {
				setBounds(previousBounds.x + dx, previousBounds.y + dy, previousBounds.width, previousBounds.height);
			}
		}
		wbp.extendPanel(getBounds());
		repaint();
		revalidate();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
			this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}
		else if(e.getX() > this.getWidth()-BORDER_WIDTH|| e.getX() < BORDER_WIDTH || e.getY() < BORDER_WIDTH|| e.getY() > this.getHeight()-BORDER_WIDTH) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

	}
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
	}
	private enum StyledNodeEditType {
		DRAG, TEXT
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		view.repaint();
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(Color.RED);
		g.fillRect(0, 0, BORDER_WIDTH, BORDER_WIDTH);

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(getWidth()-BORDER_WIDTH, getHeight()-BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
		
		
	}

}
