package networking;

import java.io.*;
import java.net.*;

/**********************************************************
 *
 * @author 
 *
 *This class handles the clients communication with the server. It receives and
 *sends information over input and output streams continuously.
 *\*********************************************************/
class Client {
    private Socket socket;
    private String userName;
    private ClientWriteThread writeThread;
    private ClientReadThread readThread;

    /*
    * These constants are the port number and host name for your server. For
    * now, the server name is set to localhost. If you wish to connect to
    * another computer, change the name to the name of that computer or IP.
    *
    * NOTE: you may need to change the port # periodically, especially if your
    * exited the application on error and did not close the socket cleanly. The
    * socket may still be in use when you try to run again and this will cause
    * otherwise working code to fail due to a busy port.
    */
    private final int port = 1224;
    private final String addrName = "localhost";

    /**********************************************************
     * TODO: Initializes the private variables
     * creates a socket (you write that method)
     * TODO: And create the two new, read & write threads.
     **********************************************************/
    public Client(String userName) {
        //init private i-vars here
    	userName = "John";

        System.out.println("Connecting as " + userName + "...");

        socket = createSocket();

        if (socket == null) {
            System.out.println("Error creating socket");
            System.exit(0);
        }

        //make threads here
        writeThread = new ClientWriteThread();
        readThread = new ClientReadThread();
    }

    /******************************************************************
    * This function should use the server port and name information to create a
    * Socket and return it.
    *
    * NOTE:Make sure to catch and report exceptions!!!
    *******************************************************************/
    public Socket createSocket() {
        InetAddress addr = null;
        Socket sock = null;

        /* TODO */

        return sock;
    }

    /******************************************************************
    * First it gets the user name as a parameter It then creates a new
    * Client
    ******************************************************************/
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: chatClient <User Name>");
            System.exit(0);
        }

        new Client(args[0]);
    }


    /***********************************************************
     * This thread is used to write to the socket
     * It has access to the Client's <socket> variable
     ***********************************************************/ 
    private class ClientWriteThread extends Thread {
        private PrintWriter writer;
        private BufferedReader stdinReader;

        /*********************************************************************
         *  Initialize both the <PrintWriter> and the <BufferedReader> classes.
         * - The <writer> should be writing to the socket (socket.getOutputStream())
         * - The <stdinReader> should be reading (AND BLOCKING) from standard input (System.in)
         * Make sure to catch exceptions.      
         ***********************************************************************/
        public ClientWriteThread() {
            /* TODO */
        }

        /**********************************************************************
         * This is what runs when the Thread is started
         * This should keep trying to read from the STDIN reader,
         * and once it gets a message it should send it to the server
         * by calling writer.println()
         ***********************************************************************/ 
        public void run() {
            String message;

            /* This is necessary for the server to know who you are */           
            writer.println(userName);

            /* TODO */
        }

    }

    /************************************************************************ 
     * This thread is used to read from a socket
     * It has access to Client's <socket> variable
     * This blocks!
     ************************************************************************/
    private class ClientReadThread extends Thread {
        private BufferedReader reader;

        /*****************************************************************
         * This should initialize the buffered Reader
         *****************************************************************/
        public ClientReadThread() {
            /* TODO */
        }

        /***********************************************************************
         * This should keep trying to read from the BufferedReader
         * The message read should be printed out with a simple System.out.println()
         * This method blocks
         * NOTE: If the server dies, reader.readLine() will be returning null!
         ************************************************************************/
        public void run() {
            String message;

            /* TODO */
        }
    }
}
