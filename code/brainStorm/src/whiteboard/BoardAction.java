package whiteboard;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction {
	protected BoardActionType type;
	protected boardnodes.BoardElt target;
	
	public boardnodes.BoardElt getTarget() {
		return target;
	}
	
	public BoardActionType getType() {
		return type;
	}
	
	public BoardAction(boardnodes.BoardElt _target) {
		target = _target;
	}
	
	public abstract String encode();
}
