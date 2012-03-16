package networking;

public class ChatMessage extends NetworkMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4666086859303673762L;
	String message;
	
	public ChatMessage(int id, String msg) {
		super(id, Type.CHAT);
		message = msg;
	}
}
