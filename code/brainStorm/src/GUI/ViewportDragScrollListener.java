package GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ViewportDragScrollListener implements MouseListener,MouseMotionListener, HierarchyListener{
    private static final int SPEED = 4;
    private final Cursor dc;
    private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final WhiteboardPanel wb;
    private Point startPt = new Point();
    private Point move    = new Point();

    public ViewportDragScrollListener(WhiteboardPanel comp) {
        this.wb = comp;
        this.dc = comp.getCursor();
    }
    @Override public void hierarchyChanged(HierarchyEvent e) {
        JComponent c = (JComponent)e.getSource();
    }
    @Override public void mouseDragged(MouseEvent e) {
    	System.out.println("dragging");
        JViewport vport = (JViewport)e.getSource();
        Point pt = e.getPoint();
        int dx = startPt.x - pt.x;
        int dy = startPt.y - pt.y;
        Point vp = vport.getViewPosition();
        vp.translate(dx, dy);
        wb.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
        move.setLocation(SPEED*dx, SPEED*dy);
        startPt.setLocation(pt);
        wb.repaint();
    }
    @Override public void mousePressed(MouseEvent e) {
        ((JComponent)e.getSource()).setCursor(hc); //label.setCursor(hc);
        startPt.setLocation(e.getPoint());
        move.setLocation(0, 0);
    }
    @Override public void mouseReleased(MouseEvent e) {
        ((JComponent)e.getSource()).setCursor(dc); //label.setCursor(dc);
    }
    @Override public void mouseExited(MouseEvent e) {
        ((JComponent)e.getSource()).setCursor(dc); //label.setCursor(dc);
        move.setLocation(0, 0);
    }
    @Override
    public void mouseClicked(MouseEvent e){
		Point offset = ((JViewport)e.getSource()).getViewPosition();
		Point wbPoint = new Point(e.getX() + offset.x, e.getY() + offset.y);
    	if (e.getModifiers() == 16) { //left click
        	wb.addNode(wbPoint);
    		
    	} else if (e.getModifiers() == 4) { //right click
    		wb.displayContextMenu(wbPoint);
    	}
    }
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
