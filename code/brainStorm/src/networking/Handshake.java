package networking;

public class Handshake extends NetworkMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1040497520979200336L;
	Integer client_id;
	String	client_username;
	public Handshake(int id, Integer _client_id, String _username) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
		client_username = _username;
	}
}