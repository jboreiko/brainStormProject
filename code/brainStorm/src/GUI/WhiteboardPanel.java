package GUI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import whiteboard.Whiteboard;

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
	private int _lastAdded;
	private ArrayList<BoardElt> _rectangles;
	private Dimension _panelSize;
	private boolean _contIns;
	private Whiteboard _board;
	
	private Point _addLocation; //the location you should add the next BoardElt to

	private JPopupMenu _rightClickMenu; //the options when a user right-clicks

	public WhiteboardPanel(){
		super();
		_lastAdded = 0;
		_board = new Whiteboard();
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(Color.BLUE);
		_contIns = true;
		_rectangles = new ArrayList<BoardElt>();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		_panelSize = new Dimension(screenSize.width, screenSize.height-100);
		setPreferredSize(_panelSize);
		setSize(_panelSize);
		_rightClickMenu = initPopupMenu();
	}
	
	public Whiteboard getBoard() {return _board;}
	
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
				StyledNode styledNode = new StyledNode(++WhiteboardPanel.UIDCounter, _board);

				System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter);
				Dimension size = styledNode.getSize();
				styledNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(styledNode);
				_lastAdded = WhiteboardPanel.STYLED;
				repaint();
			}
		});
		popup.add(styledNodeMenuItem);
		
		JMenuItem drawNodeMenuItem = new JMenuItem("Add Scribble Node");
		drawNodeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScribbleNode scribbleNode = new ScribbleNode(++WhiteboardPanel.UIDCounter, _board);

				System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter+" that should be equal to "+scribbleNode.getUID());
				Dimension size = scribbleNode.getSize();
				scribbleNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(scribbleNode);
				_board.add(scribbleNode);
				_lastAdded = WhiteboardPanel.SCRIBBLE;
				repaint();
			}
		});
		popup.add(drawNodeMenuItem);
		
		return popup;
	}
	public void addNode(Point p){
		if(_contIns){
			_addLocation = p;
			/*if(e.getX() > _panelSize.width - 200){
				Dimension newSize = new Dimension(_panelSize.width + 200,_panelSize.height);
				this.setPreferredSize(newSize);
				this.setSize(newSize);
				_panelSize = newSize;
			}
			if(e.getY() > _panelSize.height - 200){
				Dimension newSize = new Dimension(_panelSize.width,_panelSize.height + 200);
				this.setPreferredSize(newSize);
				this.setSize(newSize);
				_panelSize = newSize;
			}
			if(e.getX() < 200){
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
			if(_lastAdded == 0){
				
			}
			else if(_lastAdded == WhiteboardPanel.SCRIBBLE){
				ScribbleNode scribbleNode = new ScribbleNode(++WhiteboardPanel.UIDCounter, _board);
				System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter);
				Dimension size = scribbleNode.getSize();
				scribbleNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(scribbleNode);
				repaint();
			}
			else if(_lastAdded == WhiteboardPanel.STYLED){
				StyledNode styledNode = new StyledNode(++WhiteboardPanel.UIDCounter, _board);
				System.out.println("just created a node with UID "+WhiteboardPanel.UIDCounter);
				Dimension size = styledNode.getSize();
				styledNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
				add(styledNode);
				repaint();
			}
		}
	}
	
	public void undo() {
		_board.undo();
	}
	
	public void redo() {
		_board.redo();
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
		for(int i=0;i<_rectangles.size();i++){
			_rectangles.get(i).paintComponents(g2);
			//g2.draw(_rectangles.get(i));
			//g2.fill(_rectangles.get(i));
		}
	}
	public void load(){
		//_board.load("fileName");
	}
	public void setContinuousInsertion(boolean contIns){
		_contIns = contIns;
	}
	
}