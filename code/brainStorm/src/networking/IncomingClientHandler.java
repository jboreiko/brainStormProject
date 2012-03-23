package networking;

import java.io.*;
import java.net.*;

/**************************************************************************
 * 
 * @author 
 *
 *  The ClientHandler class is a Thread which handles all I/O with the
 *  clients. Each client will be associated with a different ClientHandler.
 *  The first line the ClientHandler reads is the client's username.
 *  Any subsequent lines are what the user is typing to the chatroom.
 *  When the client exits, you must call Server.removeClient() to inform
 *  the server that the client is no longer connected.  
 **************************************************************************/
public class IncomingClientHandler extends Thread {
    private Server server;

    private PrintWriter out;
    private BufferedReader in;

    String username;

    /***************************************************************************
     * Initialize 'server' and 'clientSocket', and create the input and output
     * streams using the socket's getInputStream() and getOutputStream() functions.
    ****************************************************************************/
    public IncomingClientHandler(Server serv, Socket clientSock) throws IOException {
        /*TODO*/
        System.out.println("Setting up handler");
    	server = serv;
    	out = new PrintWriter(clientSock.getOutputStream());
    	in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
    }

    /**************************************************************************
     * Send a message to the client.
     * @param message
     **************************************************************************/
    public void send(String message) {
        /*TODO*/
        if (message != null) {
            System.out.println("Handler sending: " + message);
            out.println(message);
            out.flush();
        }
    }

    /**************************************************************************
     * This function has been pre-implemented =).
     * @return the users name
     **************************************************************************/
    public String getUsername() {
        return username; 
    }

    /********************************************************************
     * Sign the client off.
     * Here you have to:
     * - Send a sign off message
     * - Close the input and output streams.
     * - Remove the client from the server.
      *******************************************************************/
    public void signOff() {
        /*TODO*/
    	out.write(username + " has signed off.");
    	out.close();
    	try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	server.removeClient(this);
    }

    /***********************************************************************************************
     * Read messages and broadcast them to everybody else.
     *  In a while loop:
     *  - Read a line of input using readLine().
     *  -Use the Server to broadcast it.
     *  -Remember the first message is always the username
     *  Note: If readLine() throws an exception or returns null, this means that either something
     *   went wrong or the client signed off. In either case, you want to sign off and stop the
     *   thread.  
     **********************************************************************************************/  
    public void run() {
        System.out.println("Running new handler");
        String message;
        /*TODO*/
        while(true) {
        	try {
        	    //System.out.println("Waiting to read");
        	    //System.out.flush();
        	    username = in.readLine();
				message = in.readLine();
				//System.out.println(message);
				if (message != null && username != null) {
					server.broadcastMessage(username + ": " + message);
				} else {
					throw new IOException();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.signOff();
				break;
			}
        }
    }
}
