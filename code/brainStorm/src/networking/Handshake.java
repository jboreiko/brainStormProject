package networking;

public class Handshake extends NetworkMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1040497520979200336L;
	Integer client_id;
	String	client_username;
	int START_UID;
	Object project;
	
	public Handshake(int id, Integer _client_id, String _username) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
		client_username = _username;
	}
	public Handshake(int id, Integer _client_id, String _username, int startUID, Object o) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
		client_username = _username;
		START_UID = startUID;
		project = o;
	}
	
	public void setStartUID(int id) {
		START_UID = id;
	}
	
	public int getStartUID() {
		return START_UID;
	}
}