package whiteboard;

public class DeletionAction extends BoardAction {

	public DeletionAction(BoardElt target) {
		super(target);
		type = BoardActionType.DELETION;
	}

	@Override
	public BoardAction inverse() {
		return new CreationAction(target);
	}

}
