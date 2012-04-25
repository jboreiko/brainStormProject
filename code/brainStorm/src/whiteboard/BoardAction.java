package whiteboard;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction {
	protected int target;
	protected BoardActionType type;
	
	public int getTarget() {
		return target;
	}
	
	public BoardActionType getType() {
		return type;
	}
	
	public BoardAction(int _target) {
		target = _target;
	}
	
	public abstract String encode();
}
