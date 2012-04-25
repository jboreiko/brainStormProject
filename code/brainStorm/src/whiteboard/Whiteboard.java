package whiteboard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Stack;

import networking.Networking;

import boardnodes.BoardElt.BoardEltType;

public class Whiteboard {
	private Hashtable<Integer, BoardElt> boardElts;
	private Stack<BoardAction> pastActions;
	private Stack<BoardAction> futureActions;
	private Networking networking;
	private BoardElt clipboard;
	
	
	//TODO: make every method that adds an action just add the action and then call executeaction on it (every method
	//		not including 'undo' and 'redo' need to clear the redo stack, so pass in 'true' for that parameter\
	//TODO: when calling networking.sendAction be sure to check for return false - that indicates either something went wrong (if networking is on) or networking is off
	
	//Adds the given board elt and adds the "addition" action to the stack
	public void add(BoardElt b) {
		boardElts.put(b.getUID(), b);
		pastActions.push(new CreationAction(b.getUID(), b.getType(), (int)(b.getPos().getX()), (int)(b.getPos().getY())));
	}
	
	//Adds the given board elt and does not add any action to the stack
	private void just_add(BoardElt b) {
		boardElts.put(b.getUID(), b);
	}
	
	//Returns the board elt with given UID. Returns null if no elt with that UID exists
	public BoardElt lookup(int UID) {
		return boardElts.get(UID);
	}
	
	//Returns and removes the board elt with given UID. Returns null if no elt with that UID exists
	//If the remove happens, then add a "removal" action to the stack
	public BoardElt remove(int UID) {
		BoardElt toReturn = boardElts.remove(UID);
		if(toReturn!=null) {
			pastActions.push(new DeletionAction(toReturn.getUID()));
		}
		return toReturn;
	}
	
	//Returns and removes the board elt with given UID. Returns null if no elt with that UID exists
	//Does not add a "removal" action to the stack
	public BoardElt just_remove(int UID) {
		BoardElt toReturn = boardElts.remove(UID);
		return toReturn;
	}
	
	
	//Keeps track of the event that the board element with given UID was just modified, so the whiteboard knows which
	//	one to ask to undo when necessary
	public void modifyBoardElt(int UID) {
		BoardElt b = (BoardElt) boardElts.get(UID);
		if(b!=null) {
			pastActions.push(new ModificationAction(UID));
		}
	}
	
	//Adds the given action to the stack
	public void addAction(BoardAction ba) {
		//TODO: execute action here but avoid redundancy with calls (i.e. dont execute twice)
		pastActions.push(ba);
	}
	
	//Copies the given element (i.e. sets the clipboard)
	public void copy(BoardElt b) {
		clipboard = b;
	}
	
	public void paste(Point pos) {
		BoardElt toPaste = clipboard.clone();
		toPaste.setPos(pos);
		add(toPaste);
	}
	
	public Object render() {
		//TODO: will render everything in each list. Discuss with brandon exactly how I should return this/do this
		return null;
	}
	
	private void executeAction(BoardAction b, boolean clearRedo) {
		
	}
	
	public void undo() {
		//get the top of the action stack, executeAction(its inverse), and push it to the top of the future stack
	}
	
	public void redo() {
		//get the top of the redo stack, executeAction(that thing), and push it to the top of the past stack
		BoardAction toRedo = futureActions.pop();
		executeAction(toRedo, false);
		pastActions.push(toRedo);
	}
	
	/**
	 * @author aabeshou
	 * call boardelt.encode on all of the elements in the board, and concatenate them all into one XML string that this will return
	 * @return
	 * An XML string encoding the whiteboard.
	 */
	public String encode() {
		//TODO: encode the board
		return null;
	}
	
	public static Whiteboard decode() {
		return null;
		//TODO: take in XML/JSON and create a whiteboard object out of it, compelte with all the elements that are in it
	}
	
	//returns a map from elements that contain the term to the first index of that term in the element's text field
	public Hashtable<BoardElt, Integer> search(String query) {
		Hashtable<BoardElt, Integer> toReturn = new Hashtable<BoardElt, Integer>();
		for(BoardElt b: boardElts.values()) {
			int loc = b.containsText(query);
			if(loc != -1) {
				toReturn.put(b, loc);
			}
		}
		return toReturn;
	}
	
}
