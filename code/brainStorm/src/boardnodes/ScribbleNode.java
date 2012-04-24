package boardnodes;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class ScribbleNode extends BoardElt implements MouseListener, MouseMotionListener{
	public final static int POINT_WIDTH = 3;
	public final static Point BREAK_POINT = new Point(-1,1);
	List<Point> drawnArea; //the points that have been drawn
	
	public ScribbleNode(int ID) {
		super(ID);
		drawnArea = new LinkedList<Point>();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	

	@Override
	void decode(String obj) {
		// TODO Auto-generated method stub
	}

	@Override
	String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		Iterator<Point> it = drawnArea.iterator();
		if (!it.hasNext()) return;
		Point prev = it.next();
		g.fillOval(prev.x, prev.y, POINT_WIDTH, POINT_WIDTH);
		while(it.hasNext()) {
			Point temp = it.next();
			if (temp != BREAK_POINT && prev != BREAK_POINT) {
				g.drawLine(prev.x, prev.y, temp.x, temp.y);
			}
			prev = temp;
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(300,400);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		drawnArea.add(e.getPoint());
		repaint();
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Scribble Test");
		ScribbleNode s = new ScribbleNode(0);
		f.add(s);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
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
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//the mouse has been released, stop connecting points
	@Override
	public void mouseReleased(MouseEvent e) {
		drawnArea.add(BREAK_POINT);
	}

}
