package whiteboard;

public class DeletionAction extends BoardAction {

	public DeletionAction(int id, String _targetInfo) {
		super(id, _targetInfo);
		type = BoardActionType.DELETION;
	}
	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
