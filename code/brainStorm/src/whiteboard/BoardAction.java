package whiteboard;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction {
	protected int target;
	protected BoardActionType type;
	protected String targetInfo;
	
	public int getTarget() {
		return target;
	}
	
	public BoardActionType getType() {
		return type;
	}
	
	public BoardAction(int _target, String _targetInfo) {
		target = _target;
		targetInfo = _targetInfo;
	}
	
	public String getTargetInfo() {
		return targetInfo;
	}
	
	public abstract String encode();
}
