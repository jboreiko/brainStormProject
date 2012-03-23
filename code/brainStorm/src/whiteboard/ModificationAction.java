package whiteboard;

public class ModificationAction extends BoardAction {

	BoardEltAttribute attribute;
	Object oldValue;
	Object newValue;
	
	public ModificationAction(BoardElt target, BoardEltAttribute attr, Object oldv, Object newv) {
		super(target);
		type = BoardActionType.MODIFICATION;
		attribute = attr;
		oldValue = oldv;
		newValue = newv;
	}

	@Override
	public BoardAction inverse() {
		return new ModificationAction(target, attribute, newValue, oldValue);
	}

}
