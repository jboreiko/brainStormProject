package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class ClientServer {
    private int DEFAULT_PORT = 4567;
    State currentState;
    Socket connection;
    ServerSocket server;
    LinkedList<Socket> connections;

    private enum State {
        CLIENT, SERVER, NONE
    }
    
    public ClientServer() {
        currentState = State.NONE;
        connection = null;
        server = null;
    }
    
    public boolean becomeHost() {
        currentState = State.SERVER;
        
        try {
            server = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }
    
    private void runServer() {
        
    }
    
    public boolean becomeClient(String hostIp) {
        currentState = State.CLIENT;
        try {
            connection = new Socket(hostIp, DEFAULT_PORT);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean sendMessage() {
        return false;
    }
    public boolean sendAction() {
        return false;
    }
    public boolean update() {
        return false;
    }
}
