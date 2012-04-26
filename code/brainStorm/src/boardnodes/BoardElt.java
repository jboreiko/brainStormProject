package boardnodes;

import javax.swing.JComponent;

import whiteboard.Whiteboard;
import GUI.WhiteboardPanel;
import javax.swing.JPanel;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import whiteboard.BoardActionType;

/*The parent class for all Board Elements,
 * Paths and BoardNodes*/
public abstract class BoardElt extends JPanel implements Cloneable{
	//the unique identifier of this BoardElt
	private int UID;
	//position on the board
	private double x;
	private double y;
	private String textBody;
	private static int ID_Last;
	protected WhiteboardPanel wbp;
	public BoardEltType Type;
	//the whiteboard that this is a part of
	private whiteboard.Whiteboard board;

	protected static int nextUID = 0;

	private BoardEltType type;
	public int getUID() {
		return UID;
	}
	public abstract BoardElt clone();
	public abstract void setPos(java.awt.Point p);

	public BoardElt(int _UID, whiteboard.Whiteboard w,WhiteboardPanel wbp) {
		UID = _UID;
		board = w;
		this.wbp = wbp;
	}

	public whiteboard.Whiteboard getWhiteboard() {
		return board;
	}

	public BoardEltType getType() {
		return type;
	}

	public abstract java.awt.Point getPos();


	public int containsText(String q) {
		return getText().toLowerCase().indexOf(q.toLowerCase());
	}	

	/*@param ID - the unique ID of this object
	 * @return a new BoardElt with specified ID at no particular location*/
	public BoardElt(int ID) {
		UID = ID;
		setFocusable(true);
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

	public abstract void undo();

	public abstract void redo();

	protected void notifyWhiteboard(BoardActionType b) {
		if(this.getWhiteboard()==null) {
			System.out.println("whiteboard reference is null - cannot notify it");
			return;
		}
		switch(b) {
		case CREATION:
			this.getWhiteboard().addAction(new whiteboard.CreationAction(this.getUID(), this.getType(), this.getX(), this.getY()));
			break;
		case DELETION:
			this.getWhiteboard().addAction(new whiteboard.DeletionAction(this.getUID()));
			break;
		case ELT_MOD:
			this.getWhiteboard().addAction(new whiteboard.ModificationAction(this.getUID()));
			break;
		default:
			break;
		}
		

	}

	public abstract void addAction(ActionObject ao);

	public abstract String encode();

	abstract void decode(String obj);

}
