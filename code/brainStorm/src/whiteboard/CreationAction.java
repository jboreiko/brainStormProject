package whiteboard;
import boardnodes.BoardEltType;

public class CreationAction extends BoardAction {
	
	public CreationAction(int id, String _targetInfo) {
		super(id, _targetInfo);
		type = BoardActionType.CREATION;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
