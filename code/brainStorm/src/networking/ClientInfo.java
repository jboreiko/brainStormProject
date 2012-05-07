package networking;

import java.awt.Color;
import java.io.Serializable;
import java.net.InetAddress;

public class ClientInfo implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = -6998955098723046796L;
    public InetAddress ip;
	public int id;
	public String username;
	public Color color;
	
	public ClientInfo(InetAddress _ip, int _id, String _uname, Color _color) {
		ip = _ip;
		id = _id;
		username = _uname;
		color = _color;
	}
}
