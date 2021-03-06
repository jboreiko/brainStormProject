package networking;

import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.*;

import networking.NetworkMessage.Type;

/**********************************************************
 *
 * @author 
 *
 *The entry point of your server back end. Takes in the
 *port # the server will listen on as a command line param.
 *Then listens on that port for connecting clients.
 **********************************************************/
public class Host extends Thread{
    private int localport;               //The local port the server will listen on.
    private ServerSocket serverSocket;   //The socket the server will accept connections on
    private final int MAX_CLIENTS = 6;
    private Semaphore clientRegulator; 

    private static int START_UID = 0;
    private static final int ELTS_PER_CLIENT = 5000;
    private LinkedList<ClientHandler> clients; //The list of currently connected clients.
    private LinkedList<ClientInfo> activeUsers;
    public Client localClient;
    private int hostId = 0;
    private final int portAttemptLimit = 1000;
    private ArrayList<Color> _colorarr;

    private int openId;

    /**************************************************************************
     * The constructor must initialize 'serverSocket' and 'clients'.
     * @param localport - the port the server will listen on
     * @throws IOException 
     * @throws IOException
     **************************************************************************/
    public Host(int _localport, String username, Networking net) throws IOException {
        localport = _localport;
        _colorarr = new ArrayList<Color>();
        _colorarr.add(new Color(2, 65, 237));
        _colorarr.add(new Color(2, 237, 65));
        _colorarr.add(new Color(189, 21, 9));
        _colorarr.add(new Color(100, 25, 191));
        _colorarr.add(new Color(240, 185, 5));
        _colorarr.add(new Color(247, 129, 232));
        int count = 0;
        while (true) {
            try {
                serverSocket = new ServerSocket(localport);
            } catch (IOException e) {
                //Location to catch bad port listener
                System.out.println("server: bad port at " + localport + " now trying " + (localport + 1));
                localport++;
                if (++count > portAttemptLimit) break;
                continue;
            }
            break;
        }
        clientRegulator = new Semaphore(MAX_CLIENTS);
        clients = new LinkedList<ClientHandler>();
        localClient = new Client("localhost", localport, username, net);
        activeUsers = new LinkedList<ClientInfo>();
        localClient.start();
        localClient._net._suggestPanel.setPortLabel(localport);
        openId = 1;
    }

    public boolean send(NetworkMessage nm) {
        return localClient.send(nm);
    }

    public boolean signOff() {
        if(localClient.signOff())
            return this.shutDown();
        else
            return false;
    }

    public NetworkMessage receive (Type t) {
        return localClient.receive(t);
    }

    public int getNextOpenId() {
        return openId++;
    }

    /***************************************************************************************
     * Accept connections and add them to the clients queue.
     * This function should, in a while loop:
     * - Accept a connection using ServerSocket.accept()
     * - Create a new ClientHandler, giving it the Socket that accept() returns.
     * - Add the ClientHandler to the clients list. This is so that
     * broadcastMessage() function will be able to access it.
     * - Start the ClientHandler thread so that it can begin reading any incoming messages.
     **************************************************************************************/
    public void run() {
        Socket clientSocket;
        ClientHandler handler;
        /*
        try {
			serverSocket = new ServerSocket(localport);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         */
        /*TODO*/
        while(true)	{
            try {
                System.out.println("server: waiting to accept client");
                //System.out.println("premits prior: " + clientRegulator.availablePermits());
                //clientRegulator.acquire();
                //System.out.println("premits after: " + clientRegulator.availablePermits());
                clientSocket = serverSocket.accept();
                if (clientRegulator.tryAcquire()) {
                    System.out.println("server: accepting, new client and starting new client handler");
                    handler = new ClientHandler(this, clientSocket);
                    clients.add(handler);
                    handler.start();
                } else {
                    //Currently over capacity
                    System.out.println("server: over capacity, rejecting client");
                    ClientHandler rejectHandler = new ClientHandler(this, clientSocket);
                    rejectHandler.send(new ChatMessage(0, "Apologies, the BrainStorm is currently at it's limit with " + MAX_CLIENTS + " users connected", "Host"));
                    rejectHandler.closeSocket();
                }
            } catch (IOException e) {
                System.out.println("server: closing serversocket and shuting down");
                break;
            }
        }
    }

    /***************************************************************************
     * Broadcast a message to everybody currently connected to the server by 
     * @param message - message to broadcast
     * looping through the clients list.
     * Warning: More than one ClientHandler can call this function at a time. 
     ***************************************************************************/
    public synchronized void broadcastMessage(NetworkMessage message, ClientHandler client) {
        /*TODO*/
        //System.out.println("server: broadcasting msg: " + message);
        for (ClientHandler ch: clients) {
            if (ch != client) {
                //System.out.println("server: sending msg to client: " + ch.id);
                ch.send(message);
            }
        }
    }

    public synchronized void respondHandshake(Handshake message, ClientHandler clientHandler) {
        if (message.sender_id == -1) {
            String username = message.client_username;
            for (ClientInfo ci : activeUsers) {
                if (ci.username.equals(username)) {
                    System.out.println("server: client attempted to use an already in use username :" + username);
                    clientHandler.send(message);
                    return;
                }
            }
            int temp = getNextOpenId();
            //System.out.println("server: replying to handshake with id: " + temp);
            clientHandler.setUsernameAndId(username, temp);
            START_UID += ELTS_PER_CLIENT;
            /* Send over current project */
            clientHandler.send(new Handshake(hostId, temp, username, START_UID, localClient._net.backend.saveForNetwork()));
            registerClient(clientHandler);
            System.out.println("server: registered client with id: " + temp + ", uname: " + username);
            broadcastMessage(new ChatMessage(temp, username + " just joined the Brainstorm!", "Host"), clientHandler);
        } else {
            System.out.println("server: client already has received an id ERROR");
        }
    }

    public Color pickColor() {
//    	Color color0 = new Color(2, 65, 237);
//    	Color color1 = new Color(2, 237, 65);
//    	Color color2 = new Color(189, 21, 9);
//    	Color color3 = new Color(100, 25, 191);
//    	Color color4 = new Color(240, 185, 5);
//    	Color color5 = new Color(247, 129, 232);
    	for(Color col:_colorarr) {
    		boolean use = true;
    		for(ClientInfo cl:activeUsers) {
    			if (col.equals(cl.color)) {
    				use = false;
    			}
    		}
    		if (use) {
    			return col;
    		}
    	}
        return Color.BLACK;
    }

    @SuppressWarnings("unchecked")
    private void registerClient(ClientHandler ch) {
        Color color = pickColor();
        System.out.println("COLOR:    " + color);
        activeUsers.add(new ClientInfo(ch.ip, ch.id, ch.username, color));
        System.out.println(activeUsers.size());
        broadcastMessage(new UpdateUsersMessage(0, (LinkedList<ClientInfo>) activeUsers.clone()), null);
    }
    private void unregisterClient(ClientHandler ch) {
        for (ClientInfo ci : activeUsers) {
            if (ci.id == ch.id) {
                activeUsers.remove(ci);
                broadcastMessage(new UpdateUsersMessage(0, activeUsers), null);
                break;
            }
        }
    }

    /**************************************************************************
     * Remove the given client from the clients list.
     * Warning: More than one ClientHandler can call this function at a time. 
     * Return whether the client was found or not.
     **************************************************************************/
    public synchronized boolean removeClient(ClientHandler client) {
        unregisterClient(client);
        clientRegulator.release();
        if (clients.contains(client)) {
            return clients.remove(client);
        }
        return false;
    }
    /* TODO: need to have this shut down gracefully */
    public synchronized boolean shutDown() {
        this.broadcastMessage(new ChatMessage(0, "BrainStorming session has now ended", "Host"), null);
        ClientHandler ch = null;
        while (clients.size() >= 1) {
            ch = clients.removeFirst();
            ch.shutdown();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /*
    /**************************************************************************
     * This function just creates the Server and runs it.
     * @param args - the port you will be listening on
     ************************************************************************** /
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("usage: Server portnumber");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Host s;
        try {
            s = new Host(port);
        }
        catch (IOException e) {
            System.err.println("Unable to start server: " + e);
            return;
        }

        s.run();
    }
     */
}
