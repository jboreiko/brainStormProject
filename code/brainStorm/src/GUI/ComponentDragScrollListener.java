package brainStormProject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ComponentDragScrollListener extends MouseAdapter implements HierarchyListener{
    private static final int SPEED = 4;
    private static final int DELAY = 10;
    private final Cursor dc;
    private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final Timer scroller;
    private final JComponent label;
    private Point startPt = new Point();
    private Point move    = new Point();

    public ComponentDragScrollListener(JComponent comp) {
        this.label = comp;
        this.dc = comp.getCursor();
        this.scroller = new Timer(DELAY, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                Container c = label.getParent();
                if(c instanceof JViewport) {
                    JViewport vport = (JViewport)c;
                    Point vp = vport.getViewPosition();
                    vp.translate(move.x, move.y);
                    label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
                }
            }
        });
    }
    @Override public void hierarchyChanged(HierarchyEvent e) {
        JComponent jc = (JComponent)e.getSource();
        if((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED)!=0 && !jc.isDisplayable()) {
            scroller.stop();
        }
    }
    @Override public void mouseDragged(MouseEvent e) {
        scroller.stop();
        JComponent jc = (JComponent)e.getSource();
        Container c = jc.getParent();
        if(c instanceof JViewport) {
            JViewport vport = (JViewport)jc.getParent();
            Point cp = SwingUtilities.convertPoint(jc,e.getPoint(),vport);
            int dx = startPt.x - cp.x;
            int dy = startPt.y - cp.y;
            Point vp = vport.getViewPosition();
            vp.translate(dx, dy);
            jc.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
            move.setLocation(SPEED*dx, SPEED*dy);
            startPt.setLocation(cp);
        }
    }
    @Override public void mousePressed(MouseEvent e) {
        scroller.stop();
        move.setLocation(0, 0);
        JComponent jc = (JComponent)e.getSource();
        jc.setCursor(hc);
        Container c = jc.getParent();
        if(c instanceof JViewport) {
            JViewport vport = (JViewport)c;
            Point cp = SwingUtilities.convertPoint(jc,e.getPoint(),vport);
            startPt.setLocation(cp);
        }
    }
    @Override public void mouseReleased(MouseEvent e) {
        ((JComponent)e.getSource()).setCursor(dc);
        scroller.start();
    }
    @Override public void mouseExited(MouseEvent e) {
        ((JComponent)e.getSource()).setCursor(dc);
        move.setLocation(0, 0);
        scroller.stop();
    }
}