package whiteboard;
import boardnodes.BoardEltType;

public class CreationAction extends BoardAction {

	int x;
	int y;
	BoardEltType eltType;
	
	public CreationAction(int id, BoardEltType b, int _x, int _y) {
		super(id);
		x = _x;
		y = _y;
		type = BoardActionType.CREATION;
		eltType = b;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
