package GUI;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ViewportDragScrollListener extends MouseAdapter implements HierarchyListener{
    private static final int SPEED = 4;
    private static final int DELAY = 10;
    private final Cursor dc;
    private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final JComponent panel;
    private Point startPt = new Point();
    private Point move    = new Point();

    public ViewportDragScrollListener(JComponent comp) {
        this.panel = comp;
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
        panel.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
        move.setLocation(SPEED*dx, SPEED*dy);
        startPt.setLocation(pt);
    }
    @Override public void mousePressed(MouseEvent e) {
    	Point offset = ((JViewport)e.getSource()).getViewPosition();
    	((WhiteboardPanel)(this.panel)).addRectangle(e, new Point(e.getX() + offset.x, e.getY() + offset.y));
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
}
