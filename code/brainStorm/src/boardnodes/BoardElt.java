package boardnodes;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import whiteboard.BoardActionType;
import whiteboard.SearchResult;
import GUI.ViewportDragScrollListener;
import GUI.WhiteboardPanel;

/*The parent class for all Board Elements,
 * Paths and BoardNodes*/
public abstract class BoardElt extends JPanel implements Cloneable{
	//the unique identifier of this BoardElt
	protected int UID;
	//position on the board
	private String textBody;
	private static int ID_Last;
	//the whiteboard that this is a part of
	protected whiteboard.Backend backend;
	protected WhiteboardPanel wbp;
	public ViewportDragScrollListener _mouseListener;

	protected static int nextUID = 0;

	public BoardEltType type;
	protected boolean isBeingEdited; //whether this BoardElt is in focus on ANOTHER computer
	public int getUID() {
		return UID;
	}
	public abstract BoardElt clone();

	public BoardElt(int _UID, whiteboard.Backend w) {
		UID = _UID;
		backend = w;
		wbp = backend.getPanel();
		setFocusable(true);
		hilit = new DefaultHighlighter();
	}

	public whiteboard.Backend getWhiteboard() {
		return backend;
	}

	public BoardEltType getType() {
		return type;
	}

	/*@param ID - the unique ID of this object
	 * @return a new BoardElt with specified ID at no particular location*/
	public BoardElt(int ID) {
		UID = ID;
		setFocusable(true);
		hilit = new DefaultHighlighter();
	}

	/*@param x the x location
	 * @param y the y location
	 * @return a BoardElt at the specified ID and location (x,y)*/
	public BoardElt(int ID, int x, int y) {
		UID = ID;
		this.setLocation(x, y);
		setFocusable(true);
		hilit = new DefaultHighlighter();
	}

	/*Set the initial point our */
	static void setStartingUID(int idStart) {
		ID_Last = idStart;
	}
	/*@return the body of this node as a String*/
	String getText() {
		return textBody;
	}
	
	public abstract ArrayList<SearchResult> search(String query);
	public abstract void highlightText(int index, int len, boolean isfocus);
	public abstract void clearHighlight();
	protected Highlighter hilit;

	/*Assigns this BoardElt's text to input
	 * @param toSet - what to make this Element store
	 * @return void
	 * */
	void setText(String toSet) {
		textBody = toSet;
	}

	public abstract void undo();

	public abstract void redo();

	protected void notifyBackend(BoardActionType b) {
		if(this.getWhiteboard()==null) {
			System.out.println("whiteboard reference is null - cannot notify it");
			return;
		}
		switch(b) {
		case CREATION:
			this.getWhiteboard().addAction(new whiteboard.CreationAction(this));
			break;
		case DELETION:
			this.getWhiteboard().addAction(new whiteboard.DeletionAction(this));
			break;
		case ELT_MOD:
			this.getWhiteboard().addAction(new whiteboard.ModificationAction(this));
			break;
		default:
			break;
		}
	}

	public void setBeingEditedStatus(boolean isBeingEdited) {
		System.out.println("BoardElt: EDITING STATUS CHANGED: " + isBeingEdited);
		this.isBeingEdited = isBeingEdited;
	}
	
	public abstract void addAction(ActionObject ao);
	
	public abstract SerializedBoardElt toSerialized();
	
	public abstract void ofSerialized(SerializedBoardElt b);
}
