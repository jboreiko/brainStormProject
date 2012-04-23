package networking;

import java.io.IOException;

public class NetworkingMain {
    //Welcome to our newest workspace
    public static void main(String[] args) throws IOException {
    	Networking net = new Networking();
    	//if(args[0].equals("true")) {
    	if (args[0].equals("true")) {
    		System.out.println("main: becoming host");
    		net.becomeHost("Host");
    		System.out.println("main: sending message 'test1' from localclient to host");
    		net.sendMessage("test1");
    		System.out.println("main: done sending 'test1'");
    		net.sendMessage("test2");
    		System.out.println("main: done sending 'test2'");
    	} else {
    		System.out.println("main: becoming client");
    		net.becomeClient("127.0.0.1", "Client");
            net.sendMessage("test3");
            System.out.println("main: sent 'test3'");
    	}
    }
}
