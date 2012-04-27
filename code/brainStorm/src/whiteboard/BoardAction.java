package whiteboard;

import java.io.Serializable;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction implements Serializable{
	protected BoardActionType type;
	protected boardnodes.BoardElt target;
	
	public BoardAction(boardnodes.BoardElt _target) {
	    target = _target;
	}
	
	public boardnodes.BoardElt getTarget() {
		return target;
	}
	
	public BoardActionType getType() {
		return type;
	}
	
	public abstract String encode();
}
