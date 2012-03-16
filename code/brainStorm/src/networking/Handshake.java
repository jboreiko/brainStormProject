package networking;

public class Handshake extends NetworkMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1040497520979200336L;
	Integer client_id;
	public Handshake(int id, Integer _client_id) {
		super(id, Type.HANDSHAKE);
		client_id = _client_id;
	}
}