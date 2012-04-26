package boardnodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import GUI.WhiteboardPanel;

import whiteboard.Whiteboard;


public class BoardPath extends BoardElt implements MouseListener, MouseMotionListener{
	public final static int DRAG_SQUARE_SIZE = 5; //the size of the red/green squares
	public final static Color START_COLOR = new Color(1,0,0,(float).7);
	public final static Color END_COLOR = new Color(0,1,0,(float).7);
	BoardElt _start; //where the base of the arrow points
	BoardElt _end; //where the tip of the arrow points
	double _s0, _s1;  //the location on the map of start if _start is null
	double _e0, _e1; //end location of map is _end is null
	
	int _holding; //which end you're currently dragging. 0 for start, 1 for end
	
	Point _seminal; //used for painting, see paint(Graphics)
	Point _terminal;
	
	boolean _mouseIn; //true iff the mouse is in the region of this Path
	
	/**/
	public BoardPath(int ID, Whiteboard wb, WhiteboardPanel wbp) {
		super(ID, wb, wbp);
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(new Color(0,0,0,0));
		_mouseIn = false;
		_seminal = new Point(0,0);
		_terminal = new Point(100, 100);
	}

	//set the start+end point/nodes
	public void setStart(BoardElt s) {_start = s;}
	public void setStart(double s0, double s1) {_s0 = s0; _s1 = s1;}
	public void setEnd(BoardElt e) {_end = e;}
	public void setEnd(double e0, double e1) {_e0 = e0; _e1 = e1;}
		
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		System.out.println("Painting a path");
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g.drawLine(_seminal.x, _seminal.y, _terminal.x, _terminal.y);
		if (_mouseIn) {
			g.setColor(START_COLOR);
			g.fillRect(_seminal.x-DRAG_SQUARE_SIZE/2, _seminal.y-DRAG_SQUARE_SIZE/2, DRAG_SQUARE_SIZE, DRAG_SQUARE_SIZE);
			g.setColor(END_COLOR);
			g.fillRect(_terminal.x-DRAG_SQUARE_SIZE/2, _terminal.y-DRAG_SQUARE_SIZE/2, DRAG_SQUARE_SIZE,DRAG_SQUARE_SIZE);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(_terminal.x - _seminal.x, _terminal.y - _seminal.y);
	}
	
	public final Point getSeminal() {
		return _seminal;
	}
	public final Point getTerminal() {
		return _terminal;
	}
	
	@Override
	void decode(String obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Here");
		_mouseIn = true;
		repaint();
	}
	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Gone");
		_mouseIn = false;
		repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		double startDist = (e.getX() - _seminal.x)*(e.getX() - _seminal.x) + (e.getY() - _seminal.y)*(e.getY() - _seminal.y);
		double endDist = (e.getX() - _terminal.x)*(e.getX() - _terminal.x) + (e.getY() - _terminal.y)*(e.getY() - _terminal.y);
		if (startDist < endDist) { //change the start dot location
			_holding = 0;
		} else {
			_holding = 1;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	//change the location of the start/end point (whichever is closest)
	@Override
	public void mouseDragged(MouseEvent f) {
		Point e = new Point(f.getX(), f.getY());
		if (_holding == 0) { //change the start dot location
			_seminal.x = e.x;
			_seminal.y = e.y;
		} else {
			_terminal.x = e.x;
			_terminal.y = e.y;
		}
		System.out.println("Seminal is at (" + _seminal.x + "," + _seminal.y + " and terminal at (" + _terminal.x + "," + _terminal.y +")");
		//now set the bounds of the JPanel to just barely contain the path
		setSize(Math.abs(_terminal.x - _seminal.x), Math.abs(_terminal.y - _seminal.y));
		Rectangle curLoc = getBounds();
		setBounds(curLoc.x + Math.min(_seminal.x, _terminal.x), curLoc.y + Math.min(_seminal.y, _terminal.y),
			Math.abs(_terminal.x - _seminal.x), Math.abs(_terminal.y-_seminal.y));
		if (_terminal.x > _seminal.x){
			_terminal.x -= _seminal.x;
		} else {
			_seminal.x -= _terminal.x;
		}
		if (_terminal.y > _seminal.y){
			_terminal.y -= _seminal.y;
		} else {
			_seminal.y -= _terminal.y;
		}
		if (_seminal.x <= _terminal.x) 
			_seminal.x = 0;
		else 
			_terminal.x = 0;
		if (_seminal.y <= _terminal.y)
			_seminal.y = 0;
		else 
			_terminal.y = 0;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}	
	
	/*public static void main(String[] args) {
		BoardPath b = new BoardPath(234);
		b._seminal = new Point(0,0);
		b._terminal = new Point(50,50);
		JFrame display = new JFrame("Boardpath test");
		display.add(b);
		display.pack();
		display.setVisible(true);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/

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
		//TODO: auto-generated method stub
	}
	
	@Override
	public void addAction(ActionObject ao) {
		// TODO Auto-generated method stub		
	}
}
