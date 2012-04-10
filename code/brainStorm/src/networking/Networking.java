package networking;

import java.io.IOException;

public class Networking {
    private int DEFAULT_PORT = 4567;
    State currentState;
    Client client;
    Host host;

    private enum State {
        CLIENT, HOST, NONE
    }
    
    public Networking() {
        currentState = State.NONE;
        client = null;
        host = null;
    }
    
    public boolean becomeHost() throws IOException {
        currentState = State.HOST;
        host = new Host(DEFAULT_PORT);
        host.start();
        return (host != null);
    }
    
    public boolean becomeClient(String hostIp) {
        currentState = State.CLIENT;
        client = new Client(hostIp, DEFAULT_PORT);
        client.start();
        return (client != null);
    }
    
    public boolean sendMessage(String msg) {
    	boolean ret = false;
        if (currentState == State.CLIENT) {
        	//ret = client.sendMessage(msg);
        } else if (currentState == State.HOST) {
        	//ret = host.sendMessage(msg);
        }
        return ret;
    }
    public boolean sendAction() {
        return false;
    }
    public boolean update() {
        return false;
    }
}
