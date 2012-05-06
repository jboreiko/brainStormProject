package boardnodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


import whiteboard.BoardActionType;
import whiteboard.SearchResult;

public class ScribbleNode extends BoardElt implements MouseListener, MouseMotionListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8104681433533441763L;
	boolean _resizeLock,_dragLock;
	Point startPt;
	public final static int POINT_WIDTH = 3;
	List<ColoredPoint> _pendingStroke; //the last or currentldy-being-drawn List of points
	Stack<ScribbleNodeEdit> undos;
	Stack<ScribbleNodeEdit> redos;
	JPopupMenu _drawMenu;
	Color _drawColor;
	int _drawSize;

	JPanel _scribbleArea; //the scribble area must be contained in this jpanel so we can delete and drag
	
	public final static int BORDER_WIDTH = 11; //the size of the red square signifying a delete
	
	public ScribbleNode(int ID, whiteboard.Backend w) {
		super(ID, w);
        _drawColor = Color.BLACK;
        _drawSize = 3;
        _drawMenu = new JPopupMenu();
        
        //Different Colors
        JMenu colorMenu = new JMenu("Colors");
        final String colorNames[] = 
        {"BLACK","BLUE","CYAN","DARK_GRAY","GRAY","LIGHT_GRAY","MAGENTA","ORANGE","PINK","RED","WHITE","YELLOW"};
        final Color colors[] = {Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.LIGHT_GRAY,Color.MAGENTA,
                Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};
        for(int i=0;i<colorNames.length;i+=1){
            final Color color = colors[i];
            JMenuItem drawItem = new JMenuItem(colorNames[i]);
            drawItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    _drawColor = color;
                }
            });
            colorMenu.add(drawItem);
        }
        _drawMenu.add(colorMenu);
        

        //Different Sizes
        JMenu sizeMenu = new JMenu("Size");
        final int[] sizes = {2,3,5,7,9,11,13,15};
        for (final int a : sizes) {
        	JMenuItem fontSize = new JMenuItem(a+"");
        	fontSize.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
                    _drawSize = a;
        		}
        	});
            sizeMenu.add(fontSize);
        }
        _drawMenu.add(sizeMenu);
        add(_drawMenu);
        
		_resizeLock = false;
		_dragLock = false;
		setBackground(Color.WHITE);
		undos = new Stack<ScribbleNodeEdit>();
		redos = new Stack<ScribbleNodeEdit>();
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(200,150));
		setSize(150,200);
		type = BoardEltType.SCRIBBLE;
		//_pendingStroke = new LinkedList<ColoredPoint>();
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				backend.alertEditingStatus(ScribbleNode.this, true);
			}
			@Override
			public void focusLost(FocusEvent e) {
				backend.alertEditingStatus(ScribbleNode.this, false);
			}
			
		});
		//setBorder(BorderFactory.createLineBorder(Color.GRAY,7));
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300,400);
	}

	//remove the last drawn stroke
	public void undo() {
		if (undos.empty())
			return;
		ScribbleNodeEdit f = undos.pop();
		if (f.type == ScribbleNodeEditType.DRAW) {
			List<ColoredPoint> e = (List<ColoredPoint>) f.content;
			redos.push(new ScribbleNodeEdit(e));
		} else if (f.type == ScribbleNodeEditType.DRAG) {
			Rectangle r = (Rectangle) f.content;
			redos.push(new ScribbleNodeEdit(new Rectangle(getBounds())));
			setBounds(r);
		}
	}


	@Override
	public void redo() {
		if (redos.empty()) 
			return;
		ScribbleNodeEdit f = redos.pop();
		if (f.type == ScribbleNodeEditType.DRAW) {
			List<ColoredPoint> e = (List<ColoredPoint>) f.content;
			undos.push(new ScribbleNodeEdit(e));
		} else if (f.type == ScribbleNodeEditType.DRAG) {
			Rectangle r = (Rectangle) f.content;
			undos.push(new ScribbleNodeEdit(new Rectangle(getBounds())));
			setBounds(r);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
			this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		}
		else if(e.getX() > this.getWidth()-BORDER_WIDTH|| e.getX() < BORDER_WIDTH || e.getY() < BORDER_WIDTH|| e.getY() > this.getHeight()-BORDER_WIDTH) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (withinDelete(e.getX(), e.getY())) {
			backend.remove(this.getUID());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	Rectangle boundsBeforeMove;
	@Override
	public void mousePressed(MouseEvent e) {
		wbp.setListFront(this.UID);
		this.requestFocusInWindow();
		startPt = new Point(e.getX(),e.getY());
		if(e.getX() > this.getWidth()-BORDER_WIDTH && e.getY() > this.getHeight()-BORDER_WIDTH){
			_resizeLock = true;
		}
		else if(e.getX() > this.getWidth()-BORDER_WIDTH|| e.getX() < BORDER_WIDTH || e.getY() < BORDER_WIDTH|| e.getY() > this.getHeight()-BORDER_WIDTH) {
			_dragLock = true;
		}
		else {
			LinkedList<ColoredPoint> ret = new LinkedList<ColoredPoint>();
			ColoredPoint endPt = new ColoredPoint(startPt.x+1, startPt.y+1, _drawColor, _drawSize);
	        if (e.getModifiers() == 4) {
	            _drawMenu.show(ScribbleNode.this,e.getX(),e.getY());
	        }
	        else{
				ret.add(new ColoredPoint(startPt, _drawColor, _drawSize));
	        }
			ret.add(endPt);
			_pendingStroke = ret;
		}
		boundsBeforeMove = getBounds();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		Rectangle previousBounds = getBounds();
		int dx =  (e.getX() - startPt.x);
		int dy =  (e.getY() - startPt.y);
		if(_resizeLock){
			if (e.getX() > BORDER_WIDTH*8) {//the resize leaves us with positive width
				setBounds(previousBounds.x, previousBounds.y, e.getX(), previousBounds.height);
			}
			if (e.getY()> BORDER_WIDTH*8) { //the resize leaves us with positive height
				setBounds(previousBounds.x, previousBounds.y, getBounds().width, e.getY());
			}
			wbp.extendPanel(getBounds());
			startPt.setLocation(e.getX(),e.getY());
		}
		else if(_dragLock){
			if (previousBounds.x + dx >= 0 && previousBounds.y + dy >= 0)
				setBounds(previousBounds.x + dx,previousBounds.y + dy,previousBounds.width,previousBounds.height);
			wbp.extendPanel(getBounds());
		}
		else{ //we're drawing
			if (e.getModifiers() == 16) { //left click
				_pendingStroke.add(new ColoredPoint(e.getPoint(), _drawColor, _drawSize));
			}
		}
		repaint();
		backend.getPanel().repaint();
		revalidate();
	}
	//the mouse has been released, stop connecting points
	@Override
	public void mouseReleased(MouseEvent e) {
		if (_resizeLock || _dragLock) {
			if(boundsBeforeMove.x == getBounds().x && boundsBeforeMove.y == getBounds().y
					&& boundsBeforeMove.height == getBounds().height && boundsBeforeMove.width == getBounds().width){
				_resizeLock = false;
				_dragLock = false;
				return;
			}
			undos.push(new ScribbleNodeEdit(boundsBeforeMove));
			notifyBackend(BoardActionType.ELT_MOD);
		} else {
	        if (e.getModifiers() == 4) {
	            _drawMenu.show(ScribbleNode.this,e.getX(),e.getY());
	        }
	        else{
				undos.push(new ScribbleNodeEdit(_pendingStroke));
				notifyBackend(BoardActionType.ELT_MOD);
				_pendingStroke = null;
	        }
		}
		_resizeLock = false;
		_dragLock = false;
		//if(!withinDelete(e.getX(), e.getY()))
		//	this.notifyBackend(BoardActionType.ELT_MOD);
	}
	
	public boolean withinDelete(int x, int y) {
		return (x < BORDER_WIDTH && y < BORDER_WIDTH);
	}

	@Override
	public BoardElt clone() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addAction(ActionObject ao) {
		// TODO Auto-generated method stub
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.WHITE);
		g.fillRect(BORDER_WIDTH, BORDER_WIDTH, getWidth()-2*BORDER_WIDTH, getHeight() - 2*BORDER_WIDTH);
		g.setStroke(new BasicStroke(_drawSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (ScribbleNodeEdit edit : undos) {
			if (edit.type != ScribbleNodeEditType.DRAW) { //only draw the DRAW objects
				continue;
			}
			List<ColoredPoint> stroke = (List<ColoredPoint>) edit.content;
			Iterator<ColoredPoint> it = stroke.iterator();
			if (!it.hasNext()) continue;
			ColoredPoint prev = it.next();
			while(it.hasNext()) {
				ColoredPoint temp = it.next();
				g.setColor(temp.c);
				g.setStroke(new BasicStroke(temp.s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g.drawLine(prev.x, prev.y, temp.x, temp.y);
				prev = temp;
			}
		}
		//draw the line currently being drawn
		if (!(_pendingStroke == null)){
			Iterator<ColoredPoint> it = _pendingStroke.iterator();
			if (!it.hasNext()) return;
			ColoredPoint prev = it.next();
			while(it.hasNext()) {
				ColoredPoint temp = it.next();
				g.setStroke(new BasicStroke(temp.s, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g.setColor(temp.c);
				g.drawLine(prev.x, prev.y, temp.x, temp.y);
				prev = temp;
			}
		}
		//draw the border
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), BORDER_WIDTH);
		g.fillRect(0, 0, BORDER_WIDTH, getHeight());
		g.fillRect(getWidth()-BORDER_WIDTH, 0, BORDER_WIDTH, getHeight());
		g.fillRect(0, getHeight()-BORDER_WIDTH, getWidth(), BORDER_WIDTH);
		//draw the delete square
		g.setColor(Color.RED);
		g.fillRect(0, 0, BORDER_WIDTH, BORDER_WIDTH);
		//draw the resize square
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(getWidth()-BORDER_WIDTH, getHeight()-BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
		
		if (isBeingEdited) { //visual feedback that it's being changed elsewhere
			g.setColor(Color.ORANGE);
			g.fillOval(getWidth() - BORDER_WIDTH, 0, BORDER_WIDTH, BORDER_WIDTH);
		}
	}
	
	
	public void ofSerialized(SerializedBoardElt b) {
		SerializedScribbleNode sn = (SerializedScribbleNode) b;
		UID = sn.UID;
		setBounds(sn.bounds);
		undos = sn.undos;
		redos = sn.redos;	
	}
	
	@Override
	public SerializedBoardElt toSerialized() {
		SerializedScribbleNode toReturn = new SerializedScribbleNode();
		toReturn.UID = UID;
		toReturn.bounds = (Rectangle) this.getBounds().clone();
		toReturn.undos = (Stack<ScribbleNodeEdit>) undos.clone();
		toReturn.redos = (Stack<ScribbleNodeEdit>) redos.clone();
		return toReturn;
	}
	public Color getDrawColor(){
		return _drawColor;
	}
	@Override
	public ArrayList<SearchResult> search(String query) {
		return new ArrayList<SearchResult>();
	}
	@Override
	public void highlightText(int index, int len, boolean isfocus) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clearHighlight() {
		// TODO Auto-generated method stub
	}
}
