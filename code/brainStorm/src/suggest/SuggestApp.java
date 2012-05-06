package suggest;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SuggestApp extends JFrame {
	
	
	public SuggestApp() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension interfaceSize = new Dimension(350, 2000);
		this.add(new SuggestGUI(interfaceSize, null));
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public static void main(String[] args) {
		new SuggestApp();
		//System.out.println("main is exiting");
	}
	
	
}
