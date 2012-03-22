package boardnodes;

import javax.swing.JComponent;

/*The parent class for all Board Elements,
 * Paths and BoardNodes*/
public class BoardElt extends JComponent{
	private int UID;
	
	public BoardElt(int ID) {
		UID = ID;
	}
	
}
