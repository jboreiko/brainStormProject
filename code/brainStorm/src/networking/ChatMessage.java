package networking;

public class ChatMessage extends NetworkMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4666086859303673762L;
	String text;
	String uname;
	
	public ChatMessage(String msg) {
	    super(-1, Type.CHAT);
	    text = msg;
	}
	
	public ChatMessage(int id, String msg) {
		super(id, Type.CHAT);
		text = msg;
	}
	public ChatMessage(int id, String msg, String u) {
		super(id, Type.CHAT);
		text = msg;
		uname = u;
	}
}
