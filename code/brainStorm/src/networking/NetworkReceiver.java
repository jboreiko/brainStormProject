package networking;

import boardnodes.BoardElt;

public class NetworkReceiver extends Networking implements Runnable {

	@Override
	public void run() {
		while(true) {
			//open a loop for receiving shit
			System.out.println("awaiting shit from the network");
			Object obj = receiveActionMessage();
			BoardElt b = (BoardElt) obj;
			this.backend.add(b);
		}
	}

}
