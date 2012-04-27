package whiteboard;

import java.io.Serializable;

public enum BoardActionType implements Serializable {
	CREATION, DELETION, MOVE, ELT_MOD, ELT_DO, ELT_UNDO;
}
