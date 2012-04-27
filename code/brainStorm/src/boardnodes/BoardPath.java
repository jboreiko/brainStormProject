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


public class BoardPath extends BoardElt {
	public final static int ARROW_LENGTH = 15;
	public final static double ARROW_ANGLE = Math.PI/4;
	public final static int DRAG_RADIUS = 15; //the size of the zone you can click to start dragging
	public final static int DRAG_SQUARE_RADIUS = DRAG_RADIUS/2; //the size of the marker of the zone you can click to start dragging (needs to be a bit smaller)
	final static BasicStroke dashedStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
	final static BasicStroke normalStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
	public final static Color START_COLOR = new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),210);
	public final static Color END_COLOR = new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue(),210);
	public final static Color DELETE_COLOR = Color.RED;
	public final static int START_WIDTH = 60;
	public final static int START_HEIGHT = 60;
	
	public BoardElt _snapSeminal;
	public BoardElt _snapTerminal;
	public double _snapAngle;
	
	private boolean _highlighted;
	private boolean _seminalDrag;
	private boolean _terminalDrag;
	private Stack<ActionObject> pastPositions;
	private Stack<ActionObject> futurePositions;
	private BoardPathType pathType;

	public Point _seminal; //used for painting, see paint(Graphics)
	public Point _terminal;
	private Point _oldSeminal; //used for dragging
	private Point _oldTerminal;
	
	/**/
	public BoardPath(int ID, Backend wb) {
		super(ID, wb);
		setBackground(new Color(0,0,0,0));
		_seminal = new Point(0,0);
		_terminal = new Point(100, 100);
		_oldSeminal = _seminal;
		_oldTerminal = _terminal;
		pastPositions = new Stack<ActionObject>();
		futurePositions = new Stack<ActionObject>();
		type = BoardEltType.PATH;
		_highlighted = false;
	}

	//set the start+end point/nodes
	
	public void setSeminal(Point p) { _seminal = p;}
	public void setTerminal(Point p) {_terminal = p;}
	public void setPathType(BoardPathType b) { pathType = b;}	
	
	
	public void paintComponent(Graphics graphics) {
		//super.paintComponent(graphics);
		Graphics2D g2 = (Graphics2D) graphics;
		if(_snapSeminal!=null)
			_seminal = _snapSeminal.getLocation();
		g2.setColor(Color.BLACK);
		if(pathType==BoardPathType.NORMAL || pathType==BoardPathType.ARROW) {
			g2.setStroke(normalStroke);
		} else {
			g2.setStroke(dashedStroke);
		}		
		g2.drawLine(_seminal.x, _seminal.y, _terminal.x, _terminal.y);
		g2.setStroke(normalStroke);
		if(pathType == BoardPathType.ARROW || pathType == BoardPathType.DOTTED_ARROW) {	
			Point point1 = new Point(_terminal.x, _terminal.y);
			Point point2 = new Point(_terminal.x, _terminal.y);
			double angle = Math.atan2(_seminal.y-_terminal.y, _seminal.x-_terminal.x);
			point1.x+=ARROW_LENGTH*(Math.cos(ARROW_ANGLE+angle));
			point1.y+=ARROW_LENGTH*(Math.sin(ARROW_ANGLE+angle));
			point2.x+=ARROW_LENGTH*(Math.cos(angle-ARROW_ANGLE));
			point2.y+=ARROW_LENGTH*(Math.sin(angle-ARROW_ANGLE));
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
		Point[] oldpos = new Point[]{new Point(_seminal), new Point(_terminal)};
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
		Point[] oldpos = new Point[]{new Point(_seminal), new Point(_terminal)};
		ao = new ActionObject(oldpos);
		_seminal = newpos[0];
		_terminal = newpos[1];
		futurePositions.push(ao);
	}
	
	public SerializedBoardElt toSerialized() {
		SerializedBoardPath toReturn = new SerializedBoardPath();
		toReturn.UID = UID;
		toReturn._stroke = pathType;
		toReturn._snapSeminal = _snapSeminal==null?-1:_snapSeminal.getUID();
		toReturn._snapTerminal = _snapTerminal==null?-1:_snapTerminal.getUID();
		toReturn._snapAngle = _snapAngle;
		toReturn._seminal = _seminal;
		toReturn._terminal = _terminal;
		toReturn.pastPositions = pastPositions;
		toReturn.futurePositions = futurePositions;
		return toReturn;
	}
	
	public void ofSerialized(SerializedBoardElt _future) {
		SerializedBoardPath future = (SerializedBoardPath) _future;
		UID = _future.getUID();
	    _seminal = future._seminal;
	    _terminal = future._terminal;
	    pastPositions.push(new ActionObject(new Point[]{(Point)_oldSeminal.clone(), (Point)_oldTerminal.clone()}));
	    _oldSeminal = new Point(_seminal);
	    _oldTerminal = new Point(_terminal);
	    setPathType(future._stroke);
	}

	
	@Override
	public BoardElt clone() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getUID() {
		return UID;
	}

	@Override
	public void addAction(ActionObject ao) {
		// TODO Auto-generated method stub		
	}
}
