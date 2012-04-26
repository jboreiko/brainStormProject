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
	private ArrayList<WhiteboardPanel> _whiteboards;
	private JCheckBox _contInsertionBox;
	private boolean _contInsertion;
	public InterfacePanel(ArrayList<WhiteboardPanel> whiteboards){
		super();
		_contInsertion = false;
		_whiteboards = whiteboards;
		_contInsertionBox = new JCheckBox("Continuous Insertion",null,true);
		_contInsertionBox.setVisible(true);
		_contInsertionBox.addActionListener(new ContinuousInsertionListener());
		add(_contInsertionBox);
		
	}
	
	private class ContinuousInsertionListener implements ActionListener{
		
		public ContinuousInsertionListener(){
		}
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<_whiteboards.size();i++){
				_whiteboards.get(i).setContinuousInsertion(_contInsertion);
			}
			_contInsertion = !_contInsertion;
		}
	}
}
