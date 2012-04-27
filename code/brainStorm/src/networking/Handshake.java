package networking;

public class Handshake extends NetworkMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1040497520979200336L;
	Integer client_id;
	String	client_username;
	int START_UID;
	
	public Handshake(int id, Integer _client_id, String _username) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
		client_username = _username;
	}
	public Handshake(int id, Integer _client_id, String _username, int startUID) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
		client_username = _username;
		START_UID = startUID;
	}
	
	
	public void setStartUID(int id) {
		START_UID = id;
	}
	
	public int getStartUID() {
		return START_UID;
	}
}