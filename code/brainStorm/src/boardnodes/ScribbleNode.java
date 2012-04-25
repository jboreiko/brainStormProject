package boardnodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	public final static int POINT_WIDTH = 3;
	public final static ColoredPoint BREAK_POINT = new ColoredPoint(-1,-1, Color.WHITE);
	LinkedList<List<ColoredPoint>> drawnArea; //the points that have been drawn
	LinkedList<List<ColoredPoint>> undrawnArea; //the drawn areas that have been undone

	public ScribbleNode(int ID, whiteboard.Whiteboard w) {
		super(ID, w);
		setBackground(Color.WHITE);
		drawnArea = new LinkedList<List<ColoredPoint>>();
		undrawnArea = new LinkedList<List<ColoredPoint>>();
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(200,150));
		setSize(150,200);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

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
		if (e.getModifiers() == 16) { //left click
			drawnArea.getLast().add(new ColoredPoint(e.getPoint(), Color.BLACK));
			repaint();
		} else if (e.getModifiers() == 4) { //right click
			drawnArea.getLast().add(new ColoredPoint(e.getPoint(), Color.WHITE));
			repaint();
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
		drawnArea.add(new LinkedList<ColoredPoint>());		
	}
	//the mouse has been released, stop connecting points
	@Override
	public void mouseReleased(MouseEvent e) {
		this.notifyWhiteboard(BoardActionType.ELT_MOD);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("Scribble Test");
		ScribbleNode s = new ScribbleNode(0, null);
		f.add(s);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
