package networking;

import java.io.IOException;

public class NetworkingMain {
    //Welcome to our newest workspace
    public static void main(String[] args) throws IOException {
    	Networking net = new Networking();
    	//if(args[0].equals("true")) {
    	if (false) {
    		System.out.println("Becoming host");
    		net.becomeHost();
    		System.out.println("Now sending message from localclient to host");
    		net.sendMessage("hello");
    		System.out.println("Done sending hello");
    		net.sendMessage("what is up");
    		System.out.println("Done sending whats up");
    	} else {
    		System.out.println("Becoming client");
    		net.becomeClient("127.0.0.1");
    		System.out.println("I am a client!");
            net.sendMessage("Hello");
            System.out.println("Sent Message");
    	}
    }
}
