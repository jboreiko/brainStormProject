package whiteboard;

//Represents an action on the board.
//TODO: THIS

public abstract class BoardAction {
	private BoardElt target;
	private BoardActionType type;
	private BoardNodeAttribute nodeAttr = null;
	private BoardPathAttribute pathAttr = null;
	
	public BoardElt getTarget() {
		return target;
	}
	
	public BoardAction(BoardElt target) {
	}
}
