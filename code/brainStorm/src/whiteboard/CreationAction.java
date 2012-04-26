package whiteboard;
import boardnodes.BoardEltType;

public class CreationAction extends BoardAction {
	
	public CreationAction(boardnodes.BoardElt _target) {
		super(_target);
		type = BoardActionType.CREATION;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
