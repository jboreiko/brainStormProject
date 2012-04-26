package networking;

import java.net.InetAddress;

public class ClientInfo {
	public InetAddress ip;
	public int id;
	public String username;
	
	public ClientInfo(InetAddress _ip, int _id, String _uname) {
		ip = _ip;
		id = _id;
		username = _uname;
	}
}
