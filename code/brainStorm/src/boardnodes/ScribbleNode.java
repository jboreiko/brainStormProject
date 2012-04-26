package boardnodes;

import java.awt.BasicStroke;
import whiteboard.Backend;
import GUI.WhiteboardPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import whiteboard.BoardActionType;

public class ScribbleNode extends BoardElt implements MouseListener, MouseMotionListener{
	boolean _resizeLock,_dragLock;
	Point startPt;
	public final static int POINT_WIDTH = 3;
	public final static ColoredPoint BREAK_POINT = new ColoredPoint(-1,-1, Color.WHITE);
	LinkedList<List<ColoredPoint>> drawnArea; //the points that have been drawn
	LinkedList<List<ColoredPoint>> undrawnArea; //the drawn areas that have been undone

	public ScribbleNode(int ID, whiteboard.Backend w) {
		super(ID, w);
		_resizeLock = false;
		_dragLock = false;
		setBackground(Color.WHITE);
		drawnArea = new LinkedList<List<ColoredPoint>>();
		undrawnArea = new LinkedList<List<ColoredPoint>>();
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(200,150));
		setSize(150,200);
		setBorder(BorderFactory.createLineBorder(Color.BLACK,7));
		type = BoardEltType.SCRIBBLE;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		for (List<ColoredPoint> stroke : drawnArea) {
			Iterator<ColoredPoint> it = stroke.iterator();
			if (!it.hasNext()) continue;
			ColoredPoint prev = it.next();
			while(it.hasNext()) {
				ColoredPoint temp = it.next();
				g.setColor(temp.c);
				g.drawLine(prev.x, prev.y, temp.x, temp.y);
				prev = temp;
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300,400);
	}

	//remove the last drawn stroke
	public void undo() {
		System.out.println("trying to undo");
		if(!drawnArea.isEmpty()) {
			undrawnArea.add(drawnArea.removeLast());
		}
	}


	@Override
	public void redo() {
		System.out.println("trying to undo");
		if(!undrawnArea.isEmpty()) {
			drawnArea.add(undrawnArea.removeLast());
		}
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		Rectangle nodeBounds = getBounds();
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
				repaint();
				revalidate();
				startPt.setLocation(e.getX(),e.getY());
		}
		else if(_dragLock){
			System.out.println(startPt);
			System.out.println(e.getX() + "<=====X     Y=====>" + e.getY());
			setBounds(nodeBounds.x + xoffset,nodeBounds.y + yoffset,nodeBounds.width,nodeBounds.height);
			repaint();
			revalidate();
		}
		else{
			if (e.getModifiers() == 16) { //left click
				drawnArea.getLast().add(new ColoredPoint(e.getPoint(), Color.BLACK));
				repaint();
			} else if (e.getModifiers() == 4) { //right click
				drawnArea.getLast().add(new ColoredPoint(e.getPoint(), Color.WHITE));
				repaint();
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		wbp.setListFront(this);
		startPt = new Point(e.getX(),e.getY());
		if(e.getX() > this.getWidth()-15 && e.getY() > this.getHeight()-15){
			_resizeLock = true;
		}
		else if(e.getX() > this.getWidth()-15 || e.getX() < 15 || e.getY() < 15 || e.getY() > this.getHeight()-15) {
			_dragLock = true;
		}
		else {
			LinkedList<ColoredPoint> ret = new LinkedList<ColoredPoint>();
			ret.add(new ColoredPoint(startPt, Color.BLACK));
			ColoredPoint endPt = new ColoredPoint(startPt.x+1, startPt.y+1, Color.BLACK);
			ret.add(endPt);
			drawnArea.add(ret);
		}
	}
	//the mouse has been released, stop connecting points
	@Override
	public void mouseReleased(MouseEvent e) {
		_resizeLock = false;
		_dragLock = false;
		this.notifyWhiteboard(BoardActionType.ELT_MOD);
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
		//TODO
	}
	public void addAction(ActionObject ao) {
		// TODO Auto-generated method stub

	}
}
