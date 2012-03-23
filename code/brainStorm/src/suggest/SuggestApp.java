package suggest;

import javax.swing.JFrame;

public class SuggestApp extends JFrame {
	
	
	public SuggestApp() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(new SuggestGUI());
		this.pack();
		this.setVisible(true);
		this.setResizable(false);
	}
	
	public static void main(String[] args) {
		new SuggestApp();
		//System.out.println("main is exiting");
	}
	
	
}
