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
	
	//Adds the given board elt and adds the "addition" action to the stack
	public void add(BoardElt b) {
		boardElts.put(b.getUID(), b);
		pastActions.push(new CreationAction(b));
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
			pastActions.push(new DeletionAction(toReturn));
		}
		return toReturn;
	}
	
	//Returns and removes the board elt with given UID. Returns null if no elt with that UID exists
	//Does not add a "removal" action to the stack
	public BoardElt just_remove(int UID) {
		BoardElt toReturn = boardElts.remove(UID);
		return toReturn;
	}
	
	
	//Modifies the board node with given UID, changing its given attribute to the given new value
	//Caller is responsible for passing in the correct type of newValue object and passing the UID of 
	//	a node, not a path
	public void modifyBoardNode(int UID, BoardNodeAttribute attribute, Object newValue) {
		BoardNode b = (BoardNode) boardElts.get(UID);
		if(attribute == BoardNodeAttribute.TEXT) {
			b.setText((String) newValue);
			pastActions.push(new ModificationAction(b, BoardNodeAttribute.TEXT, b.getText(), newValue));
		} else if (attribute == BoardNodeAttribute.POS) {
			b.setPos((Point) newValue);
			pastActions.push(new ModificationAction(b, BoardNodeAttribute.POS, b.getPos(), newValue));
		} else {
			System.out.println("the given attribute doesn't exist");
			return;
		}
	}
	
	//Modifies the board path with given UID, changing its given attribute to the given new value
	//Caller is responsible for passing in the correct type of newValue object and passing the UID of 
	//	a path, not a node
	public void modifyBoardPath(int UID, BoardPathAttribute attribute, Object newValue) {
		BoardPath p = ((BoardPath) boardElts.get(UID));
		if(attribute == BoardPathAttribute.TEXT) {
			p.setText((String) newValue);
			pastActions.push(new ModificationAction(p, BoardPathAttribute.TEXT, p.getText(), newValue));
		} else if (attribute == BoardPathAttribute.TYPE) {
			p.changeType((BoardPathType) newValue);
			pastActions.push(new ModificationAction(p, BoardPathAttribute.TYPE, p.getType(), newValue));
		} else if (attribute == BoardPathAttribute.POS) {
			p.setPos((Point) newValue);
			pastActions.push(new ModificationAction(p, BoardPathAttribute.POS, p.getPos(), newValue));
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
	
	private void executeAction(BoardAction b) {
		if(b.getType() == BoardActionType.CREATION) {
			this.just_add(b.getTarget());
		} else if (b.getType() == BoardActionType.DELETION) {
			this.just_remove(b.getTarget().getUID());
		} else if (b.getType() == BoardActionType.MODIFICATION) {
			//start listing attributes
			BoardElt toModify = boardElts.get(b.getTarget().getUID());
			if(toModify.getType() == BoardEltType.NODE) {
				//TODO: this is where we list out every attribute of a node and modify it as it needs to be
			} else if (toModify.getType() == BoardEltType.PATH) {
				//TODO: this is where we list out every attribute of a path and modify it as it needs to be
			}
			
			
		} else {
			//this should never happen
			return;
		}
	}
	
	public void undo() {
		//get the top of the action stack, executeAction(its inverse), and push it to the top of the future stack
		BoardAction toUndo = pastActions.pop();
		executeAction(toUndo.inverse());
		futureActions.push(toUndo);
	}
	
	public void redo() {
		//get the top of the redo stack, executeAction(that thing), and push it to the top of the past stack
		BoardAction toRedo = futureActions.pop();
		executeAction(toRedo);
		pastActions.push(toRedo);
	}
	
	public String encode() {
		//call boardelt.encode on all of the elements in the board, and concatenate them all into one XML/JSON string that this will return
	}
	
	public static Whiteboard decode() {
		//TODO: take in XML/JSON and create a whiteboard object out of it, compelte with all the elements that are in it
	}
	
}
