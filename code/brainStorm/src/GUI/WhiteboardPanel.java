package GUI;

import java.awt.*;

import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;

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
	private BoardPathType _lastPathType;
	private ArrayList<BoardElt> _elements;
	private Dimension _panelSize;
	private boolean _contIns;
	private Backend _backend;
	public ViewportDragScrollListener _mouseListener;
	public MainFrame _mainFrame;
	private Point lastClick;
	private JMenuItem pasteItem = new JMenuItem("Paste");
	private int _frontElt;
	private Point _addLocation; //the location you should add the next BoardElt to
	private BufferedImage tile;
	private boolean _contInsertion;

	private JPopupMenu _rightClickMenu; //the options when a user right-clicks

	public WhiteboardPanel(String projectName, MainFrame mf){
		super();
		_contInsertion = false;
		_mainFrame = mf;
		_lastAdded = 0;
		_backend = new Backend(projectName, this);
		_backend._mouseListener = _mouseListener;
		setFocusable(true);
		this.setLayout(null);
		this.setVisible(true);
		try {
			tile = ImageIO.read(new File("./lib/tile_blackboard_green.jpg"));
		} catch (IOException e) {
			System.out.println("no file found to tile! setting bg to gray");
			this.setBackground(Color.GRAY);
		}
		_contIns = true;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		_rightClickMenu = initPopupMenu();
	}

	public Backend getBackend() {
		return _backend;
	}

	public void displayContextMenu(Point display) {
		_addLocation = display;
		lastClick = (Point) display.clone();
		if(_backend.clipboard==null) {
			pasteItem.setEnabled(false);
		} else {
			pasteItem.setEnabled(true);
		}
		_rightClickMenu.show(this, display.x, display.y);
	}

	//initialize the right-click menu to allow for adding of nodes
	private JPopupMenu initPopupMenu() {
		JPopupMenu popup = new JPopupMenu("Context Menu");
		
		
		JMenuItem styledNodeMenuItem = new JMenuItem("Add Text Node");
		styledNodeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.STYLED, BoardPathType.NORMAL);
			}
		});
		popup.add(styledNodeMenuItem);

		JMenuItem drawNodeMenuItem = new JMenuItem("Add Scribble Node");
		drawNodeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.SCRIBBLE, BoardPathType.NORMAL);
			}
		});
		popup.add(drawNodeMenuItem);
		popup.addSeparator();
		JMenuItem addPathItem = new JMenuItem("Add Path");
		addPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.PATH, BoardPathType.NORMAL);
			}
		});
		popup.add(addPathItem);

		JMenuItem dottedPathItem = new JMenuItem("Add Dotted Path");
		dottedPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.PATH, BoardPathType.DOTTED);
			}
		});
		popup.add(dottedPathItem);

		JMenuItem arrowPathItem = new JMenuItem("Add Arrow");
		arrowPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.PATH, BoardPathType.ARROW);
			}
		});
		popup.add(arrowPathItem);

		JMenuItem dottedArrowPathItem = new JMenuItem("Add Dotted Arrow");
		dottedArrowPathItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newElt(BoardEltType.PATH, BoardPathType.DOTTED_ARROW);
			}
		});
		popup.add(dottedArrowPathItem);
		popup.addSeparator();

		pasteItem = new JMenuItem("Paste");
		pasteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_backend.paste(lastClick);
			}
		});
		final JMenuItem continuousInsertionItem = new JMenuItem("Turn Continuous Insertion On");
		continuousInsertionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_contInsertion = !_contInsertion;
				_mouseListener.setContInsertion(_contInsertion);
				continuousInsertionItem.setText("Turn Continuous Insertion "+(_contInsertion?"Off":"On"));
			}
		});
		
		popup.add(pasteItem);
		popup.add(continuousInsertionItem);
		return popup;
	}

	/**
	 * 
	 * @param obtrusion		the shape that may or may not extend beyond the bounds of this panel
	 */
	public void extendPanel(Rectangle obtrusion){
		int panelWidth = this.getSize().width;
		int panelHeight = this.getSize().height;
		if(obtrusion.x + obtrusion.width > panelWidth){ //extends past the right side
			Dimension newSize = new Dimension(obtrusion.x + obtrusion.width,panelHeight);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
		}
		if(obtrusion.y + obtrusion.height > panelHeight){ //extends down past the bottom
			Dimension newSize = new Dimension(getWidth(), obtrusion.y + obtrusion.height);
			this.setPreferredSize(newSize);
			this.setSize(newSize);
		}
	}
	public void addNode(Point p){
		if(_contIns){
			_addLocation = p;
			if(_lastAdded == 0){

			}
			else if(_lastAdded == WhiteboardPanel.SCRIBBLE){
				newElt(BoardEltType.SCRIBBLE, BoardPathType.NORMAL);
			}
			else if(_lastAdded == WhiteboardPanel.STYLED){
				newElt(BoardEltType.STYLED, BoardPathType.NORMAL);
			} else if (_lastAdded == WhiteboardPanel.PATH) {
				newElt(BoardEltType.PATH, _lastPathType);
			}
		}
		//repaint();
	}

	public void setStartUID(int id) {
		WhiteboardPanel.UIDCounter = id;
		System.out.println("uid counter is now "+WhiteboardPanel.UIDCounter);
	}

	public void addElt(BoardElt b) {
		//gives an elt a unique id, and a reference to the backend, and other than that just pushes it along adding it
		b.setUID(++WhiteboardPanel.UIDCounter);
		b.setBackend(_backend);
		add(b, 0);
		_backend.add(b);
		extendPanel(b.getBounds());
		repaint();
	}

	//boardpathtype only has to be specified when adding a path
	public BoardElt newElt(BoardEltType b, BoardPathType bpt) {
		//extendPanel(); //taken out when extendPanel changed to accept rect
		Dimension size;
		BoardElt ret = null;
		switch(b) {
		case STYLED:
			StyledNode styledNode = new StyledNode(++WhiteboardPanel.UIDCounter, _backend);
			_lastAdded = WhiteboardPanel.STYLED;
			size = styledNode.getSize();
			styledNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
			add(styledNode,0);
			_backend.add(styledNode);
			extendPanel(styledNode.getBounds());
			ret=styledNode;
			break;
		case SCRIBBLE:
			ScribbleNode scribbleNode = new ScribbleNode(++WhiteboardPanel.UIDCounter, _backend);
			_lastAdded = WhiteboardPanel.SCRIBBLE;
			size = scribbleNode.getSize();
			scribbleNode.setBounds(_addLocation.x, _addLocation.y, size.width, size.height);
			if (_addLocation.x + size.width > this.getWidth() || _addLocation.y + size.height > this.getHeight()) {
				System.out.println("Extended past our bounds");
			}
			add(scribbleNode,0);
			_backend.add(scribbleNode);
			extendPanel(scribbleNode.getBounds());
			ret=scribbleNode;
			break;
		case PATH:
			_lastAdded = WhiteboardPanel.PATH;
			_lastPathType = bpt;
			BoardPath bp = new BoardPath(++WhiteboardPanel.UIDCounter, _backend);
			size = bp.getPreferredSize();
			bp.setPathType(bpt);
			bp.setSeminal(_addLocation);
			bp.setTerminal(new Point(_addLocation.x + BoardPath.START_WIDTH, _addLocation.y + BoardPath.START_HEIGHT));
			//add(bp);
			_backend.add(bp);
			extendPanel(new Rectangle(bp.getLocation(), new Dimension(bp.getWidth(), bp.getHeight())));
			ret=bp;
			break;
		}
		repaint();
		return ret;
	}

	public void undo() {
		_backend.undo();
	}

	public void redo() {
		_backend.redo();
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(tile!=null) {
			for(int i=0;i<this.getWidth();i+=tile.getWidth()) {
				for(int j=0;j<this.getHeight();j+=tile.getHeight()) {
					g.drawImage(tile, i, j, null);
				}
			}
		}
		Graphics2D g2 = (Graphics2D)g;
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
	public void setListFront(int id){
		if(_frontElt!=id) {
			_frontElt = id;
			remove(_backend.lookup(id));
			add(_backend.lookup(id), 0);
			repaint();
		}
	}
	public void clearBoard(){
		this.removeAll();
	}

	public void extendAll() {
		for(BoardElt a : _backend.getElts())
			extendPanel(a.getBounds());
		for (BoardPath a : _backend.getPaths()) //TODO MAKE IT EXTEND ON PATHS. GETBOUNDS DOESN'T DO ANYTHING
			extendPanel(a.getBounds());
	}
}