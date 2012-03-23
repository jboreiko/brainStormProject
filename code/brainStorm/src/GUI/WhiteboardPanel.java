package brainStormProject;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

public class WhiteboardPanel extends JPanel implements MouseListener{
	private ArrayList<Rectangle> _rectangles;
	private Rectangle _rectToAdd;
	private Dimension _panelSize;
	private boolean _contIns;
	
	
	public WhiteboardPanel(){
		super();
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		_contIns = true;
		_rectangles = new ArrayList<Rectangle>();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		_panelSize = new Dimension(screenSize.width, screenSize.height-100);
		setPreferredSize(_panelSize);
		setSize(_panelSize);
		
	}
	public void mousePressed(MouseEvent e){
		if(_contIns){
			if(e.getX() > _panelSize.width - 200){
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
/*			if(e.getX() < 200){
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
	public void mouseReleased(MouseEvent e){
		
	}
	public void mouseEntered(MouseEvent e){
		
	}
	public void mouseExited(MouseEvent e){
		
	}
	public void mouseClicked(MouseEvent e){
		
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for(int i=0;i<_rectangles.size();i++){
			g2.draw(_rectangles.get(i));
			g2.fill(_rectangles.get(i));
		}
	}
	public void setContIns(boolean contIns){
		_contIns = contIns;
	}
}
