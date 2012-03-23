package brainStormProject;

import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
	private JTabbedPane _tabbedPane;
	private JMenuItem _newProject, _save, _close, _exit, _load, _undo, _redo;
	private JMenu _file, _edit;
	private JMenuBar _menuBar;
	private JPanel _interfacePane;
	private JCheckBox _contInsertion;
	
	public MainFrame(String title){
		super(title);
		this.setVisible(true);
		setJMenuBar(initMenu());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width,screenSize.height);
		_tabbedPane = new JTabbedPane();
		_tabbedPane.setVisible(true);
		_interfacePane = new JPanel();
		Dimension interfaceSize = new Dimension(250,2000);
		_interfacePane.setPreferredSize(interfaceSize);
		_interfacePane.setSize(interfaceSize);
		_interfacePane.setLayout(new GridLayout(10,0));
		_interfacePane.setVisible(true);
		_contInsertion = new JCheckBox("Continuous Insertion",null,true);
		_contInsertion.setVisible(true);
		_contInsertion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(_tabbedPane.getTabCount() == 0)){
					JScrollPane jsp = (JScrollPane)_tabbedPane.getComponent(_tabbedPane.getSelectedIndex());
				}
				else{
					JOptionPane.showMessageDialog(null, "No existing projects to close!","Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		_interfacePane.add(_contInsertion);
		getContentPane().add(_tabbedPane,BorderLayout.CENTER);
		getContentPane().add(_interfacePane,BorderLayout.WEST);
	}
	/*
	 * Method initMenu()
	 * 
	 * Arguments: none
	 * 
	 * Returns: the completed JMenu, actionlisteners and all
	 * 
	 * This method is essentially used just to clean up the constructor of MainFrame a bit.
	 * 
	 */
	public JMenuBar initMenu(){
		_menuBar = new JMenuBar();
		_menuBar.setVisible(true);
		
		//file menu
		_file = new JMenu("File");
		_file.setMnemonic(KeyEvent.VK_A);
		_file.getAccessibleContext().setAccessibleDescription("Save, load, etc.");
		_menuBar.add(_file);
		
		//new project MenuItem
		_newProject = new JMenuItem("New", KeyEvent.VK_N);
		_newProject.setMnemonic('N');
		_newProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		_newProject.getAccessibleContext().setAccessibleDescription("Creates a new project");
		_newProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.newProject
				String projectName = JOptionPane.showInputDialog(null, "Project Name","New Project",JOptionPane.PLAIN_MESSAGE);
				while(true){
					if(!((projectName.length()) < 1)){
						_save.setEnabled(true);
						_close.setEnabled(true);
						WhiteboardPanel wb = new WhiteboardPanel();
					    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
					    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
						JScrollPane scrollPane = new JScrollPane(wb,v,h);
						scrollPane.getVerticalScrollBar().setUnitIncrement(16);
						scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
						_tabbedPane.addTab(projectName, scrollPane);
						JOptionPane.showMessageDialog(null, "You clicked the New menu, and added: " + projectName);
						break;
					}
					else{
						JOptionPane.showMessageDialog(null, "Please specify a name for the project","Error!", JOptionPane.ERROR_MESSAGE);
						projectName = JOptionPane.showInputDialog(null, "Project Name","New Project",JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});
		_file.add(_newProject);
		
		//load project MenuItem
		_load = new JMenuItem("Load", KeyEvent.VK_L);
		_load.setMnemonic('L');
		_load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		_load.getAccessibleContext().setAccessibleDescription("Loads an existing project");
		_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.loadProject
				if(!(_tabbedPane.getTabCount() == 0)){
					_save.setEnabled(true);
					_close.setEnabled(true);
				}
				JOptionPane.showMessageDialog(null, "You clicked the Load menu");
			}
		});
		_file.add(_load);

		//save project MenuItem
		_save = new JMenuItem("Save", KeyEvent.VK_S);
		_save.setMnemonic('S');
		_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		_save.getAccessibleContext().setAccessibleDescription("Saves an existing project");
		_save.setEnabled(false);
		_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.loadProject
				JOptionPane.showMessageDialog(null, "You clicked the Save menu");
			}
		});
		_file.add(_save);

		//close project MenuItem
		_close = new JMenuItem("Close", KeyEvent.VK_Q);
		_close.setMnemonic('Q');
		_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		_close.getAccessibleContext().setAccessibleDescription("Closes an existing project");
		_close.setEnabled(false);
		_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(_tabbedPane.getTabCount() == 0)){
					//whiteboard.closeProject
					if(_tabbedPane.getTabCount() == 1){
						_close.setEnabled(false);
						_save.setEnabled(false);
					}
					String title = _tabbedPane.getTitleAt(_tabbedPane.getSelectedIndex());
					_tabbedPane.remove(_tabbedPane.getSelectedIndex());
					JOptionPane.showMessageDialog(null, "You clicked the Close Project menu and closed: " + title );
				}
				else{
					JOptionPane.showMessageDialog(null, "No existing projects to close!","Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		_file.add(_close);

		//quit program MenuItem
		_exit = new JMenuItem("Exit");
		_exit.getAccessibleContext().setAccessibleDescription("Exits the entire program");
		_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//something in here saying "are you sure you want to quit?"
				System.exit(1);
			}
		});
		_file.add(_exit);

		_edit = new JMenu("Edit");
		_edit.setMnemonic(KeyEvent.VK_A);
		_edit.getAccessibleContext().setAccessibleDescription("Edit things");
		_menuBar.add(_edit);

		//undo action MenuItem
		_undo = new JMenuItem("Undo", KeyEvent.VK_Z);
		_undo.setMnemonic('Z');
		_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		_undo.getAccessibleContext().setAccessibleDescription("Undoes an action");
		_undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.undo()
				JOptionPane.showMessageDialog(null, "You clicked the Undo menu");
			}
		});
		_edit.add(_undo);


		//redo action MenuItem
		_redo = new JMenuItem("Redo", KeyEvent.VK_Y);
		_redo.setMnemonic('Y');
		_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		_redo.getAccessibleContext().setAccessibleDescription("Redoes an action");
		_redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.redo()
				JOptionPane.showMessageDialog(null, "You clicked the Redo menu");
			}
		});
		_edit.add(_redo);

		
		
		return _menuBar;
	}
	public static void main(String[] args) {
		new MainFrame("BrainstormProject");
	}
}
