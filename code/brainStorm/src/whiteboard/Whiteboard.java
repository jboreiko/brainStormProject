package whiteboard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Stack;

public class Whiteboard {
	private Hashtable<Integer, BoardElt> boardElts;
	private Stack<BoardAction> pastActions;
	private Stack<BoardAction> futureActions;
	
	private BoardElt clipboard;
	
	//Adds the given board elt
	public void add(BoardElt b) {
		boardElts.put(b.getUID(), b);
	}
	
	//Returns the board elt with given UID. Returns null if no elt with that UID exists
	public BoardElt lookup(int UID) {
		return boardElts.get(UID);
	}
	
	//Returns and removes the board elt with given UID. Returns null if no elt with that UID exists
	public BoardElt remove(int UID) {
		return boardElts.remove(UID);
	}
	
	//Modifies the board node with given UID, changing its given attribute to the given new value
	//Caller is responsible for passing in the correct type of newValue object and passing the UID of 
	//	a node, not a path
	public void modifyBoardNode(int UID, BoardNodeAttribute attribute, Object newValue) {
		if(attribute == BoardNodeAttribute.TEXT) {
			((BoardNode) boardElts.get(UID)).setText((String) newValue);
		} else if (attribute == BoardNodeAttribute.POS) {
			((BoardNode) boardElts.get(UID)).setPos((Point) newValue);
		} else {
			System.out.println("the given attribute doesn't exist");
		}
	}
	
	//Modifies the board path with given UID, changing its given attribute to the given new value
	//Caller is responsible for passing in the correct type of newValue object and passing the UID of 
	//	a path, not a node
	public void modifyBoardPath(int UID, BoardPathAttribute attribute, Object newValue) {
		if(attribute == BoardPathAttribute.TEXT) {
			((BoardPath) boardElts.get(UID)).setText((String) newValue);
		} else if (attribute == BoardPathAttribute.TYPE) {
			((BoardPath) boardElts.get(UID)).changeType((BoardPathType) newValue);
		} else if (attribute == BoardPathAttribute.POS) {
			((BoardPath) boardElts.get(UID)).setPos((Point) newValue);
		} else {
			System.out.println("the given attribute doesn't exist");
		}
	}
	
	//Returns a list of boardelts that contain the given string. The caller is responsible for finding
	//	the precise index(es) of that string within the boardnode's contents
	public ArrayList<BoardElt> find(String s) {
		ArrayList<BoardElt> toReturn = new ArrayList<BoardElt>();
		Collection<BoardElt> allElts = boardElts.values();
		for(BoardElt b: allElts) {
			if(b.getText()!=null) {
				if(b.getText().indexOf(s)!=-1) {
					toReturn.add(b);
				}
			}
		}		
		return toReturn;
	}
	
	//Adds the given action to the stack
	public void addAction(BoardAction ba) {
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
	
	
}
