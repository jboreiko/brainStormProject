package whiteboard;

public class ModificationAction extends BoardAction {
	
	public ModificationAction(int target) {
		super(target);
		type = BoardActionType.MODIFICATION;
	}

	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
