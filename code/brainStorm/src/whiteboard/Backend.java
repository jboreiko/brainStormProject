package whiteboard;

import java.awt.Point;
import boardnodes.BoardElt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Stack;

import networking.Networking;

import boardnodes.BoardEltType;

public class Backend {
	private GUI.WhiteboardPanel panel;
	private Hashtable<Integer, BoardElt> boardElts;
	private ArrayList<boardnodes.BoardPath> paths;
	private Stack<BoardAction> pastActions;
	private Stack<BoardAction> futureActions;
	private Networking networking;
	private BoardElt clipboard;
	
	
	//TODO: make every method that adds an action just add the action and then call executeaction on it (every method
	//		not including 'undo' and 'redo' need to clear the redo stack, so pass in 'true' for that parameter\
	//TODO: when calling networking.sendAction be sure to check for return false - that indicates either something went wrong (if networking is on) or networking is off
	
	public Backend(GUI.WhiteboardPanel _panel) {
		panel = _panel;
		pastActions = new Stack<BoardAction>();
		futureActions = new Stack<BoardAction>();
		boardElts = new Hashtable<Integer, BoardElt>();
		paths = new ArrayList<boardnodes.BoardPath>();
		networking = new Networking();
	}
	
	//Adds the given board elt and adds the "addition" action to the stack
	public void add(BoardElt b) {
		boardElts.put(b.getUID(), b);
		if(b.getType() == BoardEltType.PATH) {
			paths.add((boardnodes.BoardPath)b);
		}
		addAction(new CreationAction(b));
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
			if(toReturn.getType()!=BoardEltType.PATH) {
				panel.remove(toReturn);
			}
			addAction(new DeletionAction(toReturn));
		}
		panel.repaint();
		return toReturn;
	}
	
	//Keeps track of the event that the board element with given UID was just modified, so the whiteboard knows which
	//	one to ask to undo when necessary
	public void modifyBoardElt(int UID) {
		BoardElt b = (BoardElt) boardElts.get(UID);
		if(b!=null) {
			addAction(new ModificationAction(b));
		}
	}
	
	//Adds the given action to the stack, and erases all future actions because we've started a new "branch"
	public void addAction(BoardAction ba) {
		System.err.println("I'm adding an action right now");
		pastActions.push(ba);
		futureActions.clear();
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
	
	public void undo() {
		//get the top of the action stack, handle it, and push it to the future actions stack for redo
		if(pastActions.empty()) {
			System.out.println("no actions to undo!");
			return;
		}
		BoardAction b = pastActions.pop();
		BoardElt be;
		switch(b.getType()) {
		case ELT_MOD:
			System.out.println("undoing a modification on node "+b.getTarget());
			System.out.println("the hashmap associates "+b.getTarget()+" with "+boardElts.get(b.getTarget()));
			b.getTarget().undo();
			b.getTarget().repaint();
			futureActions.push(b);
			break;
		case CREATION:
			//undoing a creation means doing a deletion
			 be = b.getTarget();
			if(be==null)
				return;
			panel.remove(be);
			boardElts.remove(be.getUID());
			if(be.getType()==BoardEltType.PATH) {
				paths.remove(be);
			}
			futureActions.push(new CreationAction(be));
			break;
		case DELETION:
			//undoing a deletion means doing a creation
			be = b.getTarget();
			if(be==null)
				return;
			panel.add(be);
			boardElts.put(be.getUID(), be);
			if(be.getType()==BoardEltType.PATH) {
				paths.add((boardnodes.BoardPath)be);
			}
			futureActions.push(new DeletionAction(be));
			break;
		case MOVE:
			//TODO: handle move
			break;
		}
		panel.repaint();
	}
	
	public void redo() {
		//get the top of the action stack, handle it, and push it to the past actions stack for undo
		if(futureActions.empty()) {
			System.out.println("no actions to redo!");
			return;
		}
		BoardElt be;
		BoardAction b = futureActions.pop();
		switch(b.getType()) {
		case ELT_MOD:
			b.getTarget().redo();
			b.getTarget().repaint();
			pastActions.push(b);
			break;
		case CREATION:
			//redoing a creation means doing a creation
			be = b.getTarget();
			if(be==null)
				return;
			panel.add(be);
			boardElts.put(be.getUID(), be);
			if(be.getType()==BoardEltType.PATH) {
				paths.add((boardnodes.BoardPath)be);
			}
			pastActions.push(new CreationAction(be));
			break;			
		case DELETION:
			//redoing a deletion means doing a deletion
			be = b.getTarget();
			if(be==null)
				return;
			panel.remove(be);
			boardElts.remove(be.getUID());
			if(be.getType()==BoardEltType.PATH) {
				paths.remove(be);
			}
			pastActions.push(new DeletionAction(be));
			break;
		case MOVE:
			//TODO: handle move
			break;
		}
		panel.repaint();
	}
	
	public Networking getNetworking() {
		return networking;
	}
	
	public GUI.WhiteboardPanel getPanel() {
		return panel;
	}
	
	public ArrayList<boardnodes.BoardPath> getPaths() {
		return paths;
	}
	/**
	 * @author aabeshou
	 * call boardelt.encode on all of the elements in the board, and concatenate them all into one XML string that this will return
	 * @return
	 * An XML string encoding the whiteboard.
	 */
	public String encode() {
		StringBuilder ret = new StringBuilder();
		//ret.append(Encoding.WHITEBOARD_OPEN);
		for(BoardElt b: boardElts.values()) {
			ret.append(b.encode());
		}
		//ret.append(Encoding.WHITEBOARD_CLOSE);
		return ret.toString();
	}
	
	public static Backend decode() {
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
	
	public ArrayList<BoardElt> getElts() {
		return new ArrayList<BoardElt>(boardElts.values());
	}
	
}