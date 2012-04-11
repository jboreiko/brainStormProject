package networking;

import java.io.IOException;

import networking.NetworkMessage.Type;

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
            ret = client.send(new ChatMessage(msg));
        } else if (currentState == State.HOST) {
            ret = host.send(new ChatMessage(msg));
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

    //This is blocking!!
    public String recieveChatMessage() {        
        String ret = "";
        if (currentState == State.CLIENT) {
            ret = ((ChatMessage) client.receive(Type.CHAT)).text;
        } else if (currentState == State.HOST) {
            ret = ((ChatMessage) host.receive(Type.CHAT)).text;
        }
        return ret;
    }

    //This is blocking!!
    public Object recieveActionMessage() {
        Object ret = null;
        if (currentState == State.CLIENT) {
            ret = ((ActionMessage) client.receive(Type.CHAT)).action;
        } else if (currentState == State.HOST) {
            ret = ((ActionMessage) host.receive(Type.CHAT)).action;
        }
        return ret;
    }
}
