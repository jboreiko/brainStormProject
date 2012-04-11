package networking;

import java.io.Serializable;

public class NetworkMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4817124728395759982L;
	int sender_id;
	Type type;
	protected enum Type {
		CHAT, HANDSHAKE, ACTION
	}
	protected NetworkMessage(int id, Type _type) {
		type = _type;
		if (type == Type.HANDSHAKE) {
			sender_id = -1;
		} else {
			sender_id = id;
		}
	}
	
	public void setSenderID(int _id) {
	    sender_id = _id;
	}
	
	public String toString() {
	    return "Lol I'm a network message";
	}
}
