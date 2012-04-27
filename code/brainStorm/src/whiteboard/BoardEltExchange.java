package whiteboard;

import java.io.Serializable;

import boardnodes.SerializedBoardElt;

public class BoardEltExchange implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient SerializedBoardElt affectedNode;
	private transient BoardActionType actionCommitted;
	
	public BoardEltExchange(SerializedBoardElt e, BoardActionType a) {
		affectedNode = e;
		actionCommitted = a;
	}
	
	public SerializedBoardElt getNode() {return affectedNode;}
	public BoardActionType getAction() {return actionCommitted;}
}
