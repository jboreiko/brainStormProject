package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
/**
 * 
 * @author bverch
 *
 *		This panel will house the suggest box, chat boxes, buttons, etc for the program.
 *
 */
public class InterfacePanel extends JPanel {
	private WhiteboardPanel _whiteboard;
	public InterfacePanel(WhiteboardPanel whiteboard){
		super();
		_whiteboard = whiteboard;
		
	}
}
