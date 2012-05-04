package GUI;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JViewport;

import boardnodes.BoardPath;

public class ViewportDragScrollListener implements MouseListener,MouseMotionListener, HierarchyListener{
	private static final int SPEED = 25;
	private final Cursor dc;
	private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private final WhiteboardPanel wb;
	private Point startPt = new Point();
	private Point move    = new Point();
	public BoardPath draggedPath;

	public ViewportDragScrollListener(WhiteboardPanel comp) {
		this.wb = comp;
		this.dc = comp.getCursor();
	}
	@Override public void hierarchyChanged(HierarchyEvent e) {
		JComponent c = (JComponent)e.getSource();
	}
	@Override public void mouseDragged(MouseEvent e) {
		if(draggedPath == null) {
			JViewport vport = (JViewport)e.getSource();
			Point pt = e.getPoint();
			int dx = startPt.x - pt.x;
			int dy = startPt.y - pt.y;
			Point vp = vport.getViewPosition();
			vp.translate(dx, dy);
			wb.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
			move.setLocation(SPEED*dx, SPEED*dy);
			startPt.setLocation(pt);
		} else {
			Point offset = ((JViewport)e.getSource()).getViewPosition();
			Point loc = new Point(e.getX() + offset.x, e.getY() + offset.y);
			//for(BoardPath p: wb.getBackend().getPaths()) {
			if(draggedPath.isSeminalDragging()) {
				draggedPath.setSeminal(loc);
			} else if (draggedPath.isTerminalDragging()) {
				draggedPath.setTerminal(loc);
			}
			//}
		}
		wb.repaint();
	}
	@Override public void mousePressed(MouseEvent e) {
		//first, check if we need to start dragging a path
		wb.requestFocusInWindow();
		Point offset = ((JViewport)e.getSource()).getViewPosition();
		System.out.println(offset);
		Point loc = new Point(e.getX() + offset.x, e.getY() + offset.y);
		for(BoardPath p: wb.getBackend().getPaths()) {
			if(p.isNearSeminal(loc.x, loc.y)) {
				p.startSeminalDrag();
				draggedPath = p;
				break;
			} else if(p.isNearTerminal(loc.x, loc.y)){
				p.startTerminalDrag();
				draggedPath = p;
				break;
			}
		}

		if(draggedPath==null) {
			((JComponent)e.getSource()).setCursor(hc); //label.setCursor(hc);
			startPt.setLocation(e.getPoint());
			move.setLocation(0, 0);
		}


	}
	@Override public void mouseReleased(MouseEvent e) {
		Point offset = ((JViewport)e.getSource()).getViewPosition();
		Point loc = new Point(e.getX() + offset.x, e.getY() + offset.y);
		if(draggedPath!=null) {
			draggedPath.stopDrag(loc);
			draggedPath = null;
		}
		((JComponent)e.getSource()).setCursor(dc); //label.setCursor(dc);
	}
	@Override public void mouseExited(MouseEvent e) {
		((JComponent)e.getSource()).setCursor(dc); //label.setCursor(dc);
		move.setLocation(0, 0);
	}
	@Override
	public void mouseClicked(MouseEvent e){
		Point offset = ((JViewport)e.getSource()).getViewPosition();
		Point loc = new Point(e.getX() + offset.x, e.getY() + offset.y);
		boolean deletedAPath = false;
		if (e.getModifiers() == 16) { //left click
			Iterator it = wb.getBackend().getPaths().iterator();
			while(it.hasNext()) {
				BoardPath p = (BoardPath) it.next();
				if(p.isNearDelete(loc.x, loc.y)) {
					p.delete();
					deletedAPath = true;
					it.remove();
				}
			}
			if(!deletedAPath)
				wb.addNode(loc);	
		} else if (e.getModifiers() == 4) { //right click
			wb.displayContextMenu(loc);
		}
		wb.repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseMoved(MouseEvent e) {
		Point offset = ((JViewport)e.getSource()).getViewPosition();
		Point loc = new Point(e.getX() + offset.x, e.getY() + offset.y);
		for(BoardPath p: wb.getBackend().getPaths()) {
			if(p.contains(loc.x, loc.y)) {
				p.setHighlighted(true);
			} else {
				p.setHighlighted(false);
			}
		}
		wb.repaint();
	}
}
