package whiteboard;

import java.io.Serializable;

import boardnodes.BoardElt;

public class BoardEltExchange implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BoardElt affectedNode;
	private BoardAction actionCommitted;
	
	public BoardEltExchange(BoardElt e, BoardAction a) {
		affectedNode = e;
		actionCommitted = a;
	}
	
	public BoardElt getNode() {return affectedNode;}
	public BoardAction getAction() {return actionCommitted;}
}
