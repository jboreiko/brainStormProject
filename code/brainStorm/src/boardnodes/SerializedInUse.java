package boardnodes;

import networking.ClientInfo;

/*This is a dummy SerializedBoardElt that simply
 * denotes that the BoardElt with the given UID is in use
 * and being edited on a machine*/
public class SerializedInUse extends SerializedBoardElt{
	public boolean isInUse;
	public ClientInfo sender;
	public SerializedInUse(int UID, BoardEltType type, boolean isInUse, ClientInfo sender) {
		this.UID = UID;
		this.type = type;
		this.isInUse = isInUse;
		this.sender = sender; 
	}
}
