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
import java.util.Stack;

import javax.swing.JFrame;

import GUI.WhiteboardPanel;

import whiteboard.Backend;
import whiteboard.BoardActionType;


public class BoardPath extends BoardElt implements MouseListener, MouseMotionListener{
	public final static int ARROW_LENGTH = 15;
	public final static double ARROW_ANGLE = 3*Math.PI/4;
	public final static int DRAG_RADIUS = 15; //the size of the zone you can click to start dragging
	public final static int DRAG_SQUARE_RADIUS = DRAG_RADIUS-4; //the size of the marker of the zone you can click to start dragging (needs to be a bit smaller)
	public final static Color START_COLOR = new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),210);
	public final static Color END_COLOR = new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue(),210);
	public final static Color DELETE_COLOR = Color.RED;
	public final static int START_WIDTH = 60;
	public final static int START_HEIGHT = 60;
	public BoardElt _start; //where the base of the arrow points
	public BoardElt _end; //where the tip of the arrow points
	double _s0, _s1;  //the location on the map of start if _start is null
	double _e0, _e1; //end location of map is _end is null
	private boolean _highlighted;
	private boolean _seminalDrag;
	private boolean _terminalDrag;
	private Stack<ActionObject> pastPositions;
	private Stack<ActionObject> futurePositions;
	private BoardPathType pathType;
	
	int _holding; //which end you're currently dragging. 0 for start, 1 for end
	
	public Point _seminal; //used for painting, see paint(Graphics)
	public Point _terminal;
	public Point _oldSeminal; //used for dragging
	public Point _oldTerminal;
	
	/**/
	public BoardPath(int ID, Backend wb) {
		super(ID, wb);
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(new Color(0,0,0,0));
		_seminal = new Point(0,0);
		_terminal = new Point(100, 100);
		_oldSeminal = _seminal;
		_oldTerminal = _terminal;
		pastPositions = new Stack<ActionObject>();
		futurePositions = new Stack<ActionObject>();
		type = BoardEltType.PATH;
		_highlighted = false;
		pathType = BoardPathType.ARROW;
	}

	//set the start+end point/nodes
	public void setStart(BoardElt s) {_start = s;}
	public void setStart(double s0, double s1) {_s0 = s0; _s1 = s1;}
	public void setEnd(BoardElt e) {_end = e;}
	public void setEnd(double e0, double e1) {_e0 = e0; _e1 = e1;}
	public void setSeminal(Point p) { _seminal = p;}
	public void setTerminal(Point p) {_terminal = p;}
		
	public void paintComponent(Graphics graphics) {
		//super.paintComponent(graphics);
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2.drawLine(_seminal.x, _seminal.y, _terminal.x, _terminal.y);
		if(pathType == BoardPathType.ARROW) {		
			Point point1 = new Point(_terminal.x, _terminal.y);
			Point point2 = new Point(_terminal.x, _terminal.y);
			double angle = Math.atan2(_terminal.y-_seminal.y, _terminal.x-_seminal.x);
			point1.x+=ARROW_LENGTH*(Math.cos(ARROW_ANGLE+angle));
			point1.y+=ARROW_LENGTH*(Math.sin(ARROW_ANGLE+angle));
			point2.x+=ARROW_LENGTH*(Math.cos(ARROW_ANGLE-angle));
			point2.y+=ARROW_LENGTH*(Math.sin(ARROW_ANGLE-angle));
			g2.drawLine(_terminal.x, _terminal.y, point1.x, point1.y);
			g2.drawLine(_terminal.x, _terminal.y, point2.x, point2.y);
		}
		
		if(_highlighted) {			
			g2.setColor(BoardPath.START_COLOR);
			g2.fillRect(_seminal.x-DRAG_SQUARE_RADIUS/2, _seminal.y-DRAG_SQUARE_RADIUS/2, DRAG_SQUARE_RADIUS, DRAG_SQUARE_RADIUS);
			g2.setColor(BoardPath.END_COLOR);
			g2.fillRect(_terminal.x-DRAG_SQUARE_RADIUS/2, _terminal.y-DRAG_SQUARE_RADIUS/2, DRAG_SQUARE_RADIUS, DRAG_SQUARE_RADIUS);
			Point midpt = new Point((_seminal.x + _terminal.x)/2, (_seminal.y + _terminal.y)/2);
			g2.setColor(BoardPath.DELETE_COLOR);
			g2.fillRect(midpt.x-DRAG_SQUARE_RADIUS/2, midpt.y-DRAG_SQUARE_RADIUS/2, DRAG_SQUARE_RADIUS, DRAG_SQUARE_RADIUS);
		}
	}
	
	public final Point getSeminal() {
		return _seminal;
	}
	public final Point getTerminal() {
		return _terminal;
	}

	public void delete() {
		backend.remove(this.getUID());
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
	public boolean contains(int x, int y) {
		return isNearSeminal(x,y)||isNearTerminal(x,y)||isNearDelete(x,y);
	}
	
	public boolean isNearSeminal(int x, int y) {
		return isNearPoint(x, y, _seminal);
	}
	
	public boolean isNearTerminal(int x, int y) {
		return isNearPoint(x, y, _terminal);
	}
	
	public boolean isNearDelete(int x, int y) {
		return isNearPoint(x, y, new Point((_terminal.x+_seminal.x)/2, (_terminal.y+_seminal.y)/2));
	}
	
	//helper method for the above methods
	private boolean isNearPoint(int x, int y, Point p) {
		return (Math.pow(x-p.x, 2) + Math.pow(y-p.y, 2))<=Math.pow(BoardPath.DRAG_RADIUS,2);
	}
	public void setHighlighted(boolean b) {
		_highlighted = b;
	}
	
	public void startSeminalDrag() {
		_seminalDrag = true;
		_oldSeminal = (Point) _seminal.clone();
		_oldTerminal = (Point) _terminal.clone();
	}
	
	public void startTerminalDrag() {
		_terminalDrag = true;
		_oldSeminal = (Point) _seminal.clone();
		_oldTerminal = (Point) _terminal.clone();
	}
	
	public void stopDrag(Point p) {
		if(_seminalDrag) {
			_seminal = p;
		} else if (_terminalDrag) {
			_terminal = p;
		}
		if(!_seminal.equals(_oldSeminal) || !_terminal.equals(_oldTerminal)) {
			//if either of them have changed, it's an event!
			System.out.println("it's a move event!");
			pastPositions.push(new ActionObject(new Point[]{(Point)_oldSeminal.clone(), (Point)_oldTerminal.clone()}));
			notifyBackend(BoardActionType.ELT_MOD);
		}
		_oldSeminal = (Point)_seminal.clone();
		_oldTerminal = (Point)_terminal.clone();
		_seminalDrag = false;
		_terminalDrag = false;
	}
	
	public boolean isSeminalDragging() {
		return _seminalDrag;
	}
	
	public boolean isTerminalDragging() {
		return _terminalDrag;
	}
	@Override
	public void redo() {
		//this always means reverting to a previous position, so we just pop it and set 
		//	our current position to be that one, and then push our current position to the undo stack
		if(futurePositions.empty()) {
			return;
		}
		ActionObject ao = futurePositions.pop();
		Point[] newpos = (Point[]) ao.changeInfo;
		Point[] oldpos = new Point[]{(Point) _seminal.clone(), (Point) _terminal.clone()};
		ao = new ActionObject(oldpos);
		_seminal = newpos[0];
		_terminal = newpos[1];
		pastPositions.push(ao);
	}

	@Override
	public void undo() {
		//this always means reverting to a previous position, so we just pop it and set 
		//	our current position to be that one, and then push our current position to the redo stack
		if(pastPositions.empty()) {
			return;
		}
		ActionObject ao = pastPositions.pop();
		Point[] newpos = (Point[]) ao.changeInfo;
		Point[] oldpos = new Point[]{(Point) _seminal.clone(), (Point) _terminal.clone()};
		ao = new ActionObject(oldpos);
		_seminal = newpos[0];
		_terminal = newpos[1];
		futurePositions.push(ao);
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

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
