package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	int client_id;
	ObjectInputStream ois;
    ObjectOutputStream oos;
    
	public Client(String hostIp, int port) {
		client_id = -1;
        try {
        	Socket soc = new Socket(hostIp, port);
        	System.out.println("Creates new interaction streams");
            System.out.println("Created input/output streams");
            this.connectWithHandshake(soc);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void connectWithHandshake(Socket soc) throws IOException, ClassNotFoundException {
		System.out.println("Building and sending Handshake.");
        oos = new ObjectOutputStream(soc.getOutputStream());
		oos.writeObject(new Handshake(-1, null));
		oos.flush();
		//oos.close();
		System.out.println("Handshake sent!");
        ois = new ObjectInputStream(soc.getInputStream());
		Handshake response = (Handshake) ois.readObject();
		//ois.close();
		client_id = response.client_id;
		System.out.println("Attained id " + client_id + " and am connected!");
	}
	
	public boolean sendMessage(String msg) {
		System.out.println("Client :" + client_id + " sending message.");
		try {
			oos.writeObject(new ChatMessage(client_id, msg));
			oos.flush();
			System.out.println("Message Sent!");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
