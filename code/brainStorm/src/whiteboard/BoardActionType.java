package whiteboard;

import java.io.Serializable;

public enum BoardActionType implements Serializable {
	CREATION, DELETION, ELT_MOD, REDO, UNDO, IN_USE;
}
