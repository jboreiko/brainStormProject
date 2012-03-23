package boardnodes;

import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/*The parent class for all Board Elements,
 * Paths and BoardNodes*/
public abstract class BoardElt extends JComponent implements Cloneable{
	//the unique identifier of this BoardElt
	private int UID;
	//position on the board
	private double x;
	private double y;
	private String textBody;
	private static int ID_Last;
	public BoardEltType Type;
	public enum BoardEltType {
		NODE, PATH
	}
	

	
	
	/*@param ID - the unique ID of this object
	 * @return a new BoardElt with specified ID at no particular location*/
	public BoardElt(int ID) {
		UID = ID;
	}
	
	/*@param x the x location
	 * @param y the y location
	 * @return a BoardElt at the specified ID and location (x,y)*/
	public BoardElt(int ID, double x, double y) {
		UID = ID;
		setPosition(x,y);
	}
	
	/*Set the initial point our */
	static void setStartingUID(int idStart) {
		ID_Last = idStart;
	}
	
	/*@param x the new x location
	 * @param y the new y location*/
	void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/*@return the body of this node as a String*/
	String getText() {
		return textBody;
	}
	
	
	/*Assigns this BoardElt's text to input
	 * @param toSet - what to make this Element store
	 * @return void
	 * */
	void setText(String toSet) {
		textBody = toSet;
	}
	
	
	abstract String encode();
	
	abstract void decode(String obj);
	
}
