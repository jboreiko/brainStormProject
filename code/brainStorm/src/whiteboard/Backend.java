package whiteboard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import networking.Networking;
import GUI.ViewportDragScrollListener;
import boardnodes.BoardElt;
import boardnodes.BoardEltType;
import boardnodes.BoardPath;
import boardnodes.ScribbleNode;
import boardnodes.SerializedBoardElt;
import boardnodes.SerializedBoardPath;
import boardnodes.SerializedScribbleNode;

public class Backend {
	private GUI.WhiteboardPanel panel;
	private Hashtable<Integer, BoardElt> boardElts;
	private ArrayList<boardnodes.BoardPath> paths;
	private Stack<BoardAction> pastActions;
	private Stack<BoardAction> futureActions;
	private Networking networking;
	private BoardElt clipboard;
	public ViewportDragScrollListener _mouseListener;
	
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
		networking.setBackend(this);
	}
	
	//Adds the given board elt and adds the "addition" action to the stack
	public void add(BoardElt b) {
		b._mouseListener = _mouseListener;
		boardElts.put(b.getUID(), b);
	    BoardAction committed = new CreationAction(b);
	    addAction(committed);
		if(b.getType() == BoardEltType.PATH) {
			paths.add((boardnodes.BoardPath)b);
			networking.sendAction(new BoardEltExchange(((BoardPath) b).toSerialized(), BoardActionType.CREATION));
		} else if (b.getType() == BoardEltType.SCRIBBLE){
			System.out.println("sending a scribble!");
			networking.sendAction(new BoardEltExchange(((ScribbleNode) b).toSerialized(), BoardActionType.CREATION));
		}
	}
	public void addFromNetwork(BoardElt b) {
		b._mouseListener = _mouseListener;
		boardElts.put(b.getUID(), b);
		if(b.getType() == BoardEltType.PATH)
			paths.add((boardnodes.BoardPath)b);
		
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
			BoardAction committed = new DeletionAction(toReturn);
			addAction(committed);
			if (lookup(UID) != null) {
				//networking.sendAction(new BoardEltExchange(lookup(UID), committed));
			} else  {
				System.err.println("Couldn't find UID of remove and tried to send " + UID);
			}
		}
		panel.repaint();
		return toReturn;
	}
	
	public BoardElt removeFromNetwork(int UID) {
		BoardElt toReturn = boardElts.remove(UID);
		if(toReturn!=null) {
			if(toReturn.getType()!=BoardEltType.PATH) {
				panel.remove(toReturn);
			}
			if (lookup(UID) != null) {
				//networking.sendAction(new BoardEltExchange(lookup(UID), committed));
			} else  {
				System.err.println("Couldn't find UID of remove and tried to send " + UID);
			}
		}
		panel.repaint();
		return toReturn;
	}
	
	//Keeps track of the event that the board element with given UID was just modified, so the whiteboard knows which
	//	one to ask to undo when necessary
	public void modifyBoardElt(int UID) {
		BoardElt b = (BoardElt) boardElts.get(UID);
		if(b!=null) {
			BoardAction committed = new ModificationAction(b);
			addAction(committed);
			//networking.sendAction(new BoardEltExchange(b,committed));
		}
	}
	
	//Adds the given action to the stack, and erases all future actions because we've started a new "branch"
	public void addAction(BoardAction ba) {
		System.err.println("I'm adding an action right now");
		pastActions.push(ba);
		futureActions.clear();
		if(ba.target.getType()==BoardEltType.PATH || ba.target.getType() == BoardEltType.SCRIBBLE) {
			networking.sendAction(new BoardEltExchange(ba.getTarget().toSerialized(), BoardActionType.CREATION));
		}
	}
	
	//does same thing as above except doesnt send it as a message across
	public void addActionFromNetwork(BoardAction ba) {
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
		toPaste.setLocation(pos);
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
//			boardElts   
//			System.out.println("undoing a modification on node "+b.getTarget());
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
			boardElts.remove(be.getUID());
			if(be.getType()==BoardEltType.PATH) {
				paths.remove(be);
			} else {
				panel.remove(be);
			}
			futureActions.push(new CreationAction(be));
			break;
		case DELETION:
			//undoing a deletion means doing a creation
			be = b.getTarget();
			if(be==null)
				return;
			boardElts.put(be.getUID(), be);
			if(be.getType()==BoardEltType.PATH) {
				paths.add((boardnodes.BoardPath)be);
			} else {
				panel.add(be);
			}
			futureActions.push(new DeletionAction(be));
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
			boardElts.put(be.getUID(), be);
			if(be.getType()==BoardEltType.PATH) {
				paths.add((boardnodes.BoardPath)be);
			} else {
				panel.add(be);
			}
			pastActions.push(new CreationAction(be));
			break;			
		case DELETION:
			//redoing a deletion means doing a deletion
			be = b.getTarget();
			if(be==null)
				return;
			boardElts.remove(be.getUID());
			if(be.getType()==BoardEltType.PATH) {
				paths.remove(be);
			} else {
				panel.remove(be);
			}
			pastActions.push(new DeletionAction(be));
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
		return null;
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
	
	/**callback for when the networking object associated with
	 * this backend has received a new Object reflecting a change 
	 * in the state of the hosted Whiteboard.
	 * We simply replace our outdated object with the one
	 * contained in this BoardEltExchange object
	 * 
	 * @param receivedAction
	 */
	public void receiveNetworkedObject(Object receivedAction) {
	    System.out.println("backend: received callback from networking");
		BoardEltExchange bex = (BoardEltExchange) receivedAction;
		SerializedBoardElt serializedElt = bex.getNode();
		BoardActionType type = bex.getAction();
		BoardAction action = null;
		switch (type) {
		case CREATION:
		    action = new CreationAction(receiveNetworkCreationObject(serializedElt));
		    break;
		case ELT_MOD:
		    //action = new ModificationAction();
		    break;
		case DELETION:
		    //action = new DelectionAction();
		    break;
		}
		/*
		if (nodeToReplace.getType() != BoardEltType.PATH) {
			//panel.updateMember(nodeToReplace);
		}
		else {
			for (int i = 0 ; i < paths.size(); i++) {
				if (paths.get(i).getUID() == nodeToReplace.getUID()) {
					paths.set(i, (BoardPath)nodeToReplace);
					break;
				}
			}
		}
		*/
		addActionFromNetwork(action);
		this.getPanel().repaint();
	}
	
	private BoardElt receiveNetworkCreationObject(SerializedBoardElt e) {
		BoardElt toReturn = null;
	    switch (e.getType()) {
	    case PATH:
	        if(!boardElts.containsKey(e.getUID())) {
	        	toReturn = new BoardPath(((SerializedBoardPath) e).getUID(), this);
	        	toReturn.ofSerialized(((SerializedBoardPath) e));
	        	addFromNetwork(toReturn);
	        } else {
	        	boardElts.get(((SerializedBoardPath) e).getUID()).ofSerialized(((SerializedBoardPath) e));
	        }
	        break;
	    case SCRIBBLE:
	    	System.out.println("receiving a scribble!");
	    	 if(!boardElts.containsKey(e.getUID())) {
	    		 System.out.println("adding a scribble!");
		        	toReturn = new ScribbleNode(e.getUID(), this);
		        	toReturn.ofSerialized(((SerializedScribbleNode) e));
		        	addFromNetwork(toReturn);
		        	panel.add(toReturn);
		        } else {
		        	boardElts.get(e.getUID()).ofSerialized(((SerializedScribbleNode) e));
		        }
	        break;
	    default:
	    	break;
	    }
	    panel.repaint();
        return toReturn;
    }
	
	private BoardElt receiveNetworkDeletionObject(SerializedBoardElt e) {
		BoardElt toReturn = null;
	    switch (e.getType()) {
	    case PATH:
	        if(!boardElts.containsKey(e.getUID())) {
	        	toReturn = new BoardPath(((SerializedBoardPath) e).getUID(), this);
	        	toReturn.ofSerialized(((SerializedBoardPath) e));
	        	toReturn._mouseListener = _mouseListener;
	        	boardElts.put(toReturn.getUID(), toReturn);
	        	System.out.println("adding "+toReturn.getUID());
	        	paths.add((BoardPath) toReturn);
	        }
	        break;
	    case SCRIBBLE:
	    	 if(!boardElts.containsKey(e.getUID())) {
		        	toReturn = new ScribbleNode(e.getUID(), this);
		        	toReturn.ofSerialized(((SerializedScribbleNode) e));
		        	toReturn._mouseListener = _mouseListener;
		        	boardElts.put(toReturn.getUID(), toReturn);
		        	System.out.println("adding "+toReturn.getUID());
		        } else {
		        	boardElts.get(e.getUID()).ofSerialized(((SerializedScribbleNode) e));
		        }
	        break;
	    }
	    panel.repaint();
        return toReturn;
    }
	
	private BoardElt receiveNetworkModificationObject(SerializedBoardElt e) {
		BoardElt toReturn = null;
	    switch (e.getType()) {
	    case PATH:
	        if(!boardElts.containsKey(e.getUID())) {
	        	toReturn = new BoardPath(((SerializedBoardPath) e).getUID(), this);
	        	toReturn.ofSerialized(((SerializedBoardPath) e));
	        	toReturn._mouseListener = _mouseListener;
	        	boardElts.put(toReturn.getUID(), toReturn);
	        	System.out.println("adding "+toReturn.getUID());
	        	paths.add((BoardPath) toReturn);
	        } else {
	        	boardElts.get(((SerializedBoardPath) e).getUID()).ofSerialized(((SerializedBoardPath) e));
	        }
	        break;
	    case SCRIBBLE:
	    	 if(!boardElts.containsKey(e.getUID())) {
		        	toReturn = new ScribbleNode(e.getUID(), this);
		        	toReturn.ofSerialized(((SerializedScribbleNode) e));
		        	toReturn._mouseListener = _mouseListener;
		        	boardElts.put(toReturn.getUID(), toReturn);
		        	System.out.println("adding "+toReturn.getUID());
		        } else {
		        	boardElts.get(e.getUID()).ofSerialized(((SerializedScribbleNode) e));
		        }
	        break;
	    }
	    panel.repaint();
        return toReturn;
    }

	public void setStartUID(int id) {
		System.out.println("setting start id to "+id);
		panel.setStartUID(id);
	}
    public ArrayList<BoardElt> getElts() {
		return new ArrayList<BoardElt>(boardElts.values());
	}
	
}
