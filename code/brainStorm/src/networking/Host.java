package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

import networking.NetworkMessage.Type;

public class Host {
	ServerSocket server;
	LinkedList<Socket> clients;
	int currentFreeId = 1;
	public Host(int port) {
		try {
			server = new ServerSocket(port);
            server.setSoTimeout(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clients = new LinkedList<Socket>();
	}

	public void listen() {
		while (true) {
			Socket soc = null;
			try {
			    System.out.println("Running through loop.");
				soc = server.accept();
				if (soc != null) {
					//accept new connection
					System.out.println("New connection received.");
					soc.setSoTimeout(1);
					clients.add(soc);
					Handshake id_request = null;
					while (id_request == null) {
						System.out.println("Waiting to receive handshake.");
						ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
						id_request = (Handshake) ois.readObject();
						//ois.close();
					};

					if (id_request.sender_id == -1) {
						System.out.println("Recieved proper handshake, sending it back with id.");
						id_request = new Handshake(0, currentFreeId++);
						ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
						oos.writeObject(id_request);
						oos.flush();
						//oos.close();
					} else {
						System.err.println("Fail on handshake");
					}
				}
				
				for (Socket s : clients) {
					System.out.println("Checking client.");
					ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
					NetworkMessage nm = (NetworkMessage) ois.readObject();
					if (nm != null) {
						System.out.println("Object received.");
						if (nm.type == Type.CHAT) {
							System.out.println("ChatMessage received.");
							ChatMessage cm = (ChatMessage) nm;
							System.out.println("From: " + cm.sender_id + " Message: " + cm.message);
						}
					}
				}
			} catch (SocketTimeoutException e) {

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean sendMessage(String msg) {
		// TODO Auto-generated method stub
		return false;
	}
}
