package whiteboard;

public class CreationAction extends BoardAction {

	public CreationAction(BoardElt target) {
		super(target);
		type = BoardActionType.CREATION;
	}

	@Override
	public whiteboard.BoardAction inverse() {
		return new DeletionAction(target);
	}

}
