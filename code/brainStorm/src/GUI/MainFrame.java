package GUI;



import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * @author bverch
 *
 *
 *	This is the frame that the user will see upon starting the program.
 *
 *	The user will click on the file menu and select one of the following options:
 *
 *		Create a Project: This will create a new whiteboard panel that will initialize as a host.
 *
 *		Join a Project: This will create a new whiteboard panel that will connect to a host, which will then
 *send information about the brainstormsession entered to the whiteboard panel's backend.
 *
 *		Load Project: This will create a new whiteboard panel that will load its backend from an xml(?)
 *
 *		Save Project: This will save the whiteboard panel's backend to an xml(?)
 *
 *		Close Project: This will close a project, prompting the user to save first.
 *
 *		Exit Brainstorm: This will exit the program, prompting the user to save first.
 *
 *
 */

public class MainFrame extends JFrame {
	private JTabbedPane _tabbedPane;
	private JMenuItem _newProject, _save, _close, _exit, _load, _undo, _redo, _join;
	private JMenu _file, _edit;
	private JMenuBar _menuBar;
	private InterfacePanel _interfacePane;
	private ArrayList<WhiteboardPanel> _whiteboards;
	private WhiteboardPanel _activeBoardPanel;
	
	/*
	 * Mainframe()
	 * 
	 * Initializes the menus and list of whiteboardpanels.
	 * 
	 * 
	 */
	
	
	public MainFrame(String title){
		super(title);
		this.setVisible(true);
		setJMenuBar(initMenu());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width,screenSize.height);
		_tabbedPane = new JTabbedPane();
		_tabbedPane.setVisible(true);
		_whiteboards = new ArrayList<WhiteboardPanel>();
		getContentPane().add(_tabbedPane,BorderLayout.CENTER);
		_interfacePane = new InterfacePanel(_whiteboards);
		Dimension interfaceSize = new Dimension(250,2000);
		_interfacePane.setPreferredSize(interfaceSize);
		_interfacePane.setSize(interfaceSize);
		_interfacePane.setLayout(new GridLayout(10,0));
		_interfacePane.setVisible(true);
        add(_tabbedPane, BorderLayout.CENTER);
		add(_interfacePane, BorderLayout.WEST);
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
		_newProject = new JMenuItem("Create a Project", KeyEvent.VK_N);
		_newProject.setMnemonic('N');
		_newProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		_newProject.getAccessibleContext().setAccessibleDescription("Creates a new project");
		_newProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.newProject
				String projectName = JOptionPane.showInputDialog(null, "Project Name","New Project",JOptionPane.PLAIN_MESSAGE);
				if(projectName==null) {
					System.out.println("project creation was cancelled");
					return;
				}
				while(true){
					if(!((projectName.length()) < 1)){
						_save.setEnabled(true);
						_close.setEnabled(true);
						WhiteboardPanel wb = new WhiteboardPanel();
						_whiteboards.add(wb);
					    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
					    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
						JScrollPane scrollPane = new JScrollPane(wb,v,h);
						//interesting stuff
						ViewportDragScrollListener l = new ViewportDragScrollListener(wb);
						JViewport vp = scrollPane.getViewport();
						vp.addMouseMotionListener(l);
						vp.addMouseListener(l);
						vp.addHierarchyListener(l);
						_tabbedPane.addTab(projectName, scrollPane);
						JOptionPane.showMessageDialog(null, "You clicked the New Project menu, and added: " + projectName);
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
		

		//join project MenuItem
		_join = new JMenuItem("Join Project", KeyEvent.VK_L);
		_join.setMnemonic('J');
		_join.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK));
		_join.getAccessibleContext().setAccessibleDescription("Joins an existing project");
		_join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//whiteboard.joinProject
				if(!(_tabbedPane.getTabCount() == 0)){
					_save.setEnabled(true);
					_close.setEnabled(true);
				}
				JOptionPane.showMessageDialog(null, "You clicked the Join Project menu");
			}
		});
		_file.add(_join);
		
		//load project MenuItem
		_load = new JMenuItem("Load Project", KeyEvent.VK_L);
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
		_save = new JMenuItem("Save Project", KeyEvent.VK_S);
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
		_close = new JMenuItem("Close Project", KeyEvent.VK_Q);
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
		_exit = new JMenuItem("Exit Brainstorm");
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
				//CLICKING UNDO METHOD
				WhiteboardPanel selectedPane = _whiteboards.get(_tabbedPane.getSelectedIndex());
				if(selectedPane==null) {
					System.out.println("no pane is selected!");
					return;
				}				
				selectedPane.undo();
				//JOptionPane.showMessageDialog(null, "You clicked the Undo menu");
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
				//CLICKING REDO METHOD
				WhiteboardPanel selectedPane = _whiteboards.get(_tabbedPane.getSelectedIndex());
				if(selectedPane==null) {
					System.out.println("no pane is selected!");
					return;
				}				
				selectedPane.redo();
			}
		});
		_edit.add(_redo);

		
		
		return _menuBar;
	}
	public static void main(String[] args) {
		new MainFrame("BrainstormProject");
	}
}
