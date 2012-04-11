package networking;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import networking.NetworkMessage.Type;

/**********************************************************
 *
 * @author 
 *
 *This class handles the clients communication with the server. It receives and
 *sends information over input and output streams continuously.
 *\*********************************************************/
class Client extends Thread{
    public int clientId;
    
    private Socket socket;
    private ClientWriteThread writeThread;
    private ClientReadThread readThread;
    private LinkedBlockingQueue<NetworkMessage> toSend; 
    private LinkedBlockingQueue<ChatMessage> chatReceived; 
    private LinkedBlockingQueue<ActionMessage> actionReceived;
    private Semaphore lock;

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
    private int port = 1337;
    private String addrName = "localhost";

    /**********************************************************
     * TODO: Initializes the private variables
     * creates a socket (you write that method)
     * TODO: And create the two new, read & write threads.
     **********************************************************/
    public Client(String _hostIp, int _port) {
        //init private i-vars here
    	//userName = _userName;

        //System.out.println("Connecting as " + userName + "...");
        
        toSend = new LinkedBlockingQueue<NetworkMessage>();
        chatReceived = new LinkedBlockingQueue<ChatMessage>();
        actionReceived = new LinkedBlockingQueue<ActionMessage>();
        lock = new Semaphore(0);
    	
    	port = _port;
    	addrName = _hostIp;
    	clientId = -1;

        socket = createSocket();

        if (socket == null) {
            System.out.println("Error creating socket");
            System.exit(0);
        }
    }

    /******************************************************************
    * This function should use the server port and name information to create a
    * Socket and return it.
    *
    * NOTE:Make sure to catch and report exceptions!!!
    *******************************************************************/
    public Socket createSocket() {
        //InetAddress addr = null;
        Socket sock = null;

        /* TODO */
        try {
            sock = new Socket(addrName, port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sock;
    }
    
    public boolean send (NetworkMessage nm) {
        try {
            lock.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Current id before sending: " + clientId);
        nm.setSenderID(clientId);
        return toSend.offer(nm);
    }
    
    public NetworkMessage receive (Type t) {
        if (t == Type.CHAT) {
            return chatReceived.poll();
        } else if (t == Type.ACTION) {
            return actionReceived.poll();
        }
        return null;
    }
    
    public void run() {
        //make threads here
        writeThread = new ClientWriteThread();
        readThread = new ClientReadThread(this);
        
        //System.out.println("Running threads");
        writeThread.start();
        readThread.start();
        try {
            readThread.join();
            writeThread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /******************************************************************
    * First it gets the user name as a parameter It then creates a new
    * Client
    ******************************************************************/
    /*
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: chatClient <User Name>");
            System.exit(0);
        }
        new Client(args[0]);
    }
    */


    /***********************************************************
     * This thread is used to write to the socket
     * It has access to the Client's <socket> variable
     ***********************************************************/ 
    private class ClientWriteThread extends Thread {
        //private PrintWriter writer;
        //private BufferedReader stdinReader;
        private ObjectOutputStream writer;
        
        /*********************************************************************
         *  Initialize both the <PrintWriter> and the <BufferedReader> classes.
         * - The <writer> should be writing to the socket (socket.getOutputStream())
         * - The <stdinReader> should be reading (AND BLOCKING) from standard input (System.in)
         * Make sure to catch exceptions.      
         ***********************************************************************/
        public ClientWriteThread() {
            /* TODO */
            try {
                //writer = new PrintWriter(socket.getOutputStream());
                writer = new ObjectOutputStream(socket.getOutputStream());
                //stdinReader = new BufferedReader(new InputStreamReader(System.in));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**********************************************************************
         * This is what runs when the Thread is started
         * This should keep trying to read from the STDIN reader,
         * and once it gets a message it should send it to the server
         * by calling writer.println()
         ***********************************************************************/ 
        public void run() {
            NetworkMessage message = null;
            
            try {
                //Get Id
                System.out.println("Requesting id, currently I have " + clientId);
                writer.writeObject(new Handshake(clientId, -1));
                writer.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            while(true) {
                //System.out.println("Print your message");
                //message = stdinReader.readLine();
                System.out.println("Reader is polling now.");
                message = toSend.poll();
                System.out.println("Reader is done polling.");
                if (message != null) {
                /* This is necessary for the server to know who you are */          
                    //System.out.println(userName + " sending: " + message);
                    //writer.println(userName);
                    //writer.flush();
                    try {
                        System.out.println("Writing out message");
                        writer.writeObject(message);
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Writing thread has quit!");
                    break;
                }
                /* TODO */
            }
        }

    }

    /************************************************************************ 
     * This thread is used to read from a socket
     * It has access to Client's <socket> variable
     * This blocks!
     ************************************************************************/
    private class ClientReadThread extends Thread {
        //private BufferedReader reader;
        private ObjectInputStream reader;
        private Client parent;
        
        /*****************************************************************
         * This should initialize the buffered Reader
         *****************************************************************/
        public ClientReadThread(Client _parent) {
            /* TODO */
            parent = _parent;
            try {
                //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                reader = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /***********************************************************************
         * This should keep trying to read from the BufferedReader
         * The message read should be printed out with a simple System.out.println()
         * This method blocks
         * NOTE: If the server dies, reader.readLine() will be returning null!
         ************************************************************************/
        public void run() {
            NetworkMessage message = null;
            while (true) {
                try {
                    //System.out.println("Waiting for message...");
                    //message = reader.readLine();
                    message = (NetworkMessage) reader.readObject();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (message != null) {
                    //System.out.println(message);
                    if (message.type == Type.ACTION) {
                        actionReceived.offer((ActionMessage) message);
                    } else if (message.type == Type.CHAT) {
                        System.out.println("Received chat message: " + ((ChatMessage) message).text);
                        chatReceived.offer((ChatMessage) message);
                    } else if (message.type == Type.HANDSHAKE) {
                        clientId = ((Handshake) message).client_id;
                        System.out.println("Update client id to: " + clientId);
                        parent.lock.release();
                    }
                } else {
                    System.err.println("Failure reading thread has quit!");
                    break;
                }
            }
            /* TODO */
        }
    }
}
