package networking;

public class NetworkingMain {
    //Welcome to our newest workspace
    public static void main(String[] args) {
    	Networking net = new Networking();
    	//if(args[0].equals("true")) {
    	if (false) {
    		System.out.println("Becoming host");
    		net.becomeHost();
    	} else {
    		System.out.println("Becoming client");
    		net.becomeClient("127.0.0.1");
    		System.out.println("I am a client!");
            net.client.sendMessage("Hello");
            System.out.println("Sent Message");
    	}
    }
}
