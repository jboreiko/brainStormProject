package GUI;

import java.awt.*;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import whiteboard.Backend;
import networking.Networking;

import boardnodes.*;
/**
 * 
 * @author bverch
 * 
 * 		This is the main Whiteboard Panel. It basically is the class where all of the other aspects
 * of the project come together. The suggest and chat box will be added here, and 
 * 
 * 
 * 
 * 
 */
public class WhiteboardPanel extends JPanel{
	/**
	 * 
	 */
	public static int UIDCounter = 0;
	public static final int STYLED = 1;
	public static final int SCRIBBLE = 2;
	public static final int PATH = 3;
	private int _lastAdded;
	private ArrayList<BoardElt> _elements;
	private Dimension _panelSize;
	private boolean _contIns;
	private Backend _backend;
	
	private Point _addLocation; //the location you should add the next BoardElt to

	private JPopupMenu _rightClickMenu; //the options when a user right-clicks

	public WhiteboardPanel(){
		super();
		_lastAdded = 0;
		_backend = new Backend(this);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(Color.BLUE);
		_contIns = true;
		_elements = new ArrayList<BoardElt>();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		_panelSize = new Dimension(screenSize.width, screenSize.height-100);
		setPreferredSize(_panelSize);
		setSize(_panelSize);
		_rightClickMenu = initPopupMenu();
	}
	
	public Backend getBackend() {
		return _backend;
	}
	
	public void displayContextMenu(Point display) {
		_addLocation = display;
		_rightClickMenu.show(this, display.x, display.y);
	}
	
	//initialize the right-click menu to allow for adding of nodes
	private JPopupMenu initPopupMenu() {
		JPopupMenu popup = new JPopupMenu("Context Menu");
		JMenuItem styledNodeMenuItem = new JMenuItem("Add Styled Node");
		styledNodeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.STYLED);
				/*extendPanel();
				StyledNode styledNode = new StyledNode(++WhiteboardPanel.UIDCounter, _board,WhiteboardPanel.this);
				Dimension size = styledNode.getSize();
				styledNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(styledNode);
				_board.add(styledNode);
				_lastAdded = WhiteboardPanel.STYLED;
				repaint();*/
			}
		});
		popup.add(styledNodeMenuItem);
		
		JMenuItem drawNodeMenuItem = new JMenuItem("Add Scribble Node");
		drawNodeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.SCRIBBLE);
				/*_lastAdded = WhiteboardPanel.SCRIBBLE;
				ScribbleNode scribbleNode = new ScribbleNode(++WhiteboardPanel.UIDCounter, _board,WhiteboardPanel.this);
				System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter+" that should be equal to "+scribbleNode.getUID());
				Dimension size = scribbleNode.getSize();
				scribbleNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(scribbleNode);
				_board.add(scribbleNode);
				repaint();*/
			}
		});
		popup.add(drawNodeMenuItem);
		
		JMenuItem addPathItem = new JMenuItem("Add Path");
		addPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.PATH);
			}
		});
		popup.add(addPathItem);
		
		return popup;
	}
	
	/**
	 * 
	 * @param obtrusion		the shape that may or may not extend beyond the bounds of this panel
	 */
	public void extendPanel(Rectangle obtrusion){
		if(obtrusion.x + obtrusion.width > _panelSize.width){ //extends past the right side
			Dimension newSize = new Dimension(obtrusion.x + obtrusion.width,_panelSize.height);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
			_panelSize = newSize;
		}
		if(obtrusion.y + obtrusion.height > _panelSize.height){ //extends down past the bottom
			Dimension newSize = new Dimension(_panelSize.width, obtrusion.y + obtrusion.height);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
			_panelSize = newSize;
		}
		/*if(e.getX() < 200){
			//SOME KIND OF TRANSLATION HERE
			Dimension newSize = new Dimension(_panelSize.width + 200,_panelSize.height);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
			_panelSize = newSize;
		}
		if(e.getY() < 200){
			//SOME KIND OF TRANSLATION HERE
			Dimension newSize = new Dimension(_panelSize.width,_panelSize.height + 200);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
			_panelSize = newSize;
		}*/
	}
	public void addNode(Point p){
		if(_contIns){
			_addLocation = p;
//			extendPanel();
			if(_lastAdded == 0){
				
			}
			else if(_lastAdded == WhiteboardPanel.SCRIBBLE){
				newElt(BoardEltType.SCRIBBLE);
			}
			else if(_lastAdded == WhiteboardPanel.STYLED){
				newElt(BoardEltType.STYLED);
			} else if (_lastAdded == WhiteboardPanel.PATH) {
				newElt(BoardEltType.PATH);
			}
		}
		//repaint();
	}
	
	private void newElt(BoardEltType b) {
		//extendPanel(); //taken out when extendPanel changed to accept rect
		Dimension size;
		switch(b) {
		case STYLED:
			System.out.println("trying to add a styled");
			StyledNode styledNode = new StyledNode(++WhiteboardPanel.UIDCounter, _backend);
			_lastAdded = WhiteboardPanel.STYLED;
			System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter);
			size = styledNode.getSize();
			styledNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
			add(styledNode,0);
			_backend.add(styledNode);
			extendPanel(styledNode.getBounds());
			break;
		case SCRIBBLE:
			System.out.println("trying to add a scribble");
			ScribbleNode scribbleNode = new ScribbleNode(++WhiteboardPanel.UIDCounter, _backend);
			_lastAdded = WhiteboardPanel.SCRIBBLE;
			System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter);
			size = scribbleNode.getSize();
			scribbleNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
			if (_addLocation.x + size.width > this.getWidth() || _addLocation.y + size.height > this.getHeight()) {
				System.out.println("Extended past our bounds");
			}
			System.out.println("wide " + getWidth() + ", height " + getHeight());
			add(scribbleNode,0);
			_backend.add(scribbleNode);
			extendPanel(scribbleNode.getBounds());
			break;
		case PATH:
			System.out.println("trying to add a path");
			_lastAdded = WhiteboardPanel.PATH;
			BoardPath bp = new BoardPath(++WhiteboardPanel.UIDCounter, _backend);
			System.out.println("Just created a path");
			size = bp.getPreferredSize();
			System.out.println(size);
			bp.setSeminal(_addLocation);
			bp.setTerminal(new Point(_addLocation.x + BoardPath.START_WIDTH, _addLocation.y + BoardPath.START_HEIGHT));
			//add(bp);
			_backend.add(bp);
			extendPanel(bp.getBounds());
			break;
		}
		repaint();
	}
	
	public void undo() {
		_backend.undo();
	}
	
	public void redo() {
		_backend.redo();
	}

/*	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction){
		int currentPosition = 0;
		if(orientation == SwingConstants.HORIZONTAL){
			currentPosition = visibleRect.x;
		}
		else{
			currentPosition = visibleRect.y;
		}
		
		if(direction < 0){
			int newPosition = currentPosition - (currentPosition / _increment)*_increment;
			return(newPosition == 0) ? _increment : newPosition;
		}
		else{
			return ((currentPosition / _increment) + 1) * _increment - currentPosition;
			
		}
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction){
		if (orientation == SwingConstants.HORIZONTAL){
			return visibleRect.width - _increment;
		}
		else{
			return visibleRect.height - _increment;
		}
	}
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	public boolean getScrollableTracksViewportHeight(){
		return false;
	}
	public void setIncrement(int pixels){
		_increment = pixels;
	}*/
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for(int i=0;i<_elements.size();i++){
			_elements.get(i).paintComponents(g2);
			//g2.draw(_rectangles.get(i));
			//g2.fill(_rectangles.get(i));
		}
		for(BoardPath b: _backend.getPaths()) {
			b.paintComponent(g2);
		}
	}
	public void load(){
		//_board.load("fileName");
	}
	public void setContinuousInsertion(boolean contIns){
		_contIns = contIns;
	}
	public void setListFront(BoardElt element){
		remove(element);
		add(element, 0);
		repaint();
	}
}