package GUI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
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
	private ArrayList<Rectangle> _rectangles;
	private Rectangle _rectToAdd;
	private Dimension _panelSize;
	private boolean _contIns;
	private int _tx,_ty;
	private int _oldX,_oldY;
	private int _increment;
	
	
	public WhiteboardPanel(){
		super();
		_tx = 0;
		_ty = 0;
		_increment = 1;
		//_board = new Whiteboard();
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		_contIns = true;
		_rectangles = new ArrayList<Rectangle>();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		_panelSize = new Dimension(screenSize.width, screenSize.height-100);
		setPreferredSize(_panelSize);
		setSize(_panelSize);
	}
	public void addRectangle(MouseEvent e,Point p){
		if(_contIns){
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
			_rectToAdd = new Rectangle((int)e.getX(),(int)e.getY(),100,100);
			_rectangles.add(_rectToAdd);
			repaint();
		}
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
			g2.draw(_rectangles.get(i));
			g2.fill(_rectangles.get(i));
		}
	}
	public void setContinuousInsertion(boolean contIns){
		_contIns = contIns;
	}
}
