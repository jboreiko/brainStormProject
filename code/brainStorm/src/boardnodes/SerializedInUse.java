package boardnodes;

/*This is a dummy SerializedBoardElt that simply
 * denotes that the BoardElt with the given UID is in use
 * and being edited on a machine*/
public class SerializedInUse extends SerializedBoardElt{
	public boolean isInUse;
	public SerializedInUse(int UID, BoardEltType type, boolean isInUse) {
		this.UID = UID;
		this.type = type;
		this.isInUse = isInUse;
	}
}
