package networking;

import java.io.IOException;

import suggest.SuggestGUI;

import whiteboard.Backend;

import networking.NetworkMessage.Type;

public class Networking {
	public int DEFAULT_PORT = 4568;//4567;
	State currentState;
	Client client;
	Host host;
	Backend backend;
	SuggestGUI _suggestPanel;
	//NetworkReceiver receiver;
	Thread receiverThread;

	private enum State {
		CLIENT, HOST, NONE
	}

	public Networking() {
		currentState = State.NONE;
		client = null;
		host = null;
	}

	public void setBackend(Backend b) {
		if(backend==null) {
			backend = b;
		}
	}
	
	public Backend getBackend() {
		return backend;
	}
	public void setSuggestPanel(SuggestGUI suggest) {
		_suggestPanel = suggest;
	}
	
	public void startReceiving() {
		//receiver = new NetworkReceiver();
		//receiver.backend = backend;
		//receiverThread = new Thread(receiver);
		//receiverThread.start();
	}

	public boolean becomeHost(String username) {
		currentState = State.HOST;
		try {
			host = new Host(DEFAULT_PORT, username, this);
		} catch (IOException e) {
			System.out.println("server: failed to create socket listener and connect client");
			return false;
		}
		host.start();
		return (host != null);
	}

	public boolean becomeClient(String hostIp, String username, int port) {
		currentState = State.CLIENT;
		try {
			client = new Client(hostIp, port, username, this);
		} catch (IOException e) {
			System.out.println("client: failed to connect socket");
			return false;
		}
		client.start();
		//startReceiving();
		return (client != null);
	}

	public boolean sendMessage(String msg) {
		boolean ret = false;
		if (currentState == State.CLIENT) {
			ret = client.send(new ChatMessage(client.clientId, msg, client.username));
		} else if (currentState == State.HOST) {
			ret = host.send(new ChatMessage(host.localClient.clientId, msg, host.localClient.username));
		}
		return ret;
	}
	public boolean sendAction(Object o) {       
		boolean ret = false;
		if (currentState == State.CLIENT) {
			ret = client.send(new ActionMessage(o));
		} else if (currentState == State.HOST) {
			ret = host.send(new ActionMessage(o));
		}
		return ret;
	}
	/* NEEDS TO BE FIXED IN THE CLIENT */
	public boolean signOff() {
		boolean ret = false;
		if (currentState == State.CLIENT) {
			ret = client.signOff();
		} else if (currentState == State.HOST) {
			ret = host.signOff();
		}
		currentState = State.NONE;
		client = null;
		host = null;
		return ret;
	}

	//This is blocking!!
	public String receiveChatMessage() {        
		String ret = "";
		System.out.println("received message");
		if (currentState == State.CLIENT) {
			ret = ((ChatMessage) client.receive(Type.CHAT)).text;
		} else if (currentState == State.HOST) {
			ret = ((ChatMessage) host.receive(Type.CHAT)).text;
		}
		return ret;
	}

	//This is blocking!!
	public Object receiveActionMessage() {
		Object ret = null;
		if (currentState == State.CLIENT) {
			ret = ((ActionMessage) client.receive(Type.ACTION)).action;
		} else if (currentState == State.HOST) {
			ret = ((ActionMessage) host.receive(Type.ACTION)).action;
		}
		System.out.println(ret);
		return ret;
	}
}
