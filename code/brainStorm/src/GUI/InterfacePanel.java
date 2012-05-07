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
	private JCheckBox _contInsertionBox;
	public InterfacePanel(WhiteboardPanel whiteboard){
		super();
		_whiteboard = whiteboard;
		_contInsertionBox = new JCheckBox("Continuous Insertion",null,true);
		_contInsertionBox.setVisible(true);
		_contInsertionBox.addActionListener(new ContinuousInsertionListener());
		add(_contInsertionBox);
		
	}
	
	private class ContinuousInsertionListener implements ActionListener{
		
		public ContinuousInsertionListener(){
		}
		public void actionPerformed(ActionEvent e) {
			System.out.println("changing");
			_whiteboard._mouseListener.toggleContInsertion();
		}
	}
}
