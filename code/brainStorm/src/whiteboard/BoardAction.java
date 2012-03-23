package whiteboard;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction {
	protected BoardElt target;
	protected BoardActionType type;
	
	public BoardElt getTarget() {
		return target;
	}
	
	public BoardActionType getType() {
		return type;
	}
	
	public BoardAction(BoardElt _target) {
		target = _target;
	}
	
	//returns the inverse action for execution
	public abstract BoardAction inverse();
}
