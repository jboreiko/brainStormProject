package boardnodes;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StyledNode extends JPanel{
	JEditorPane content;
	
	public StyledNode(){
		content = createEditorPane();
		this.add(content);
	}
	
	
	private JEditorPane createEditorPane() {
		return new JEditorPane("text/rtf", "asdfasdfasdf");
	}
	
	
	
	/*This exists just to let me peek at progress incrementally*/
	public static void main(String[] args){
		JFrame node = new JFrame("Node debug");
		StyledNode a = new StyledNode();
		a.setVisible(true);
		node.add(a);
		node.pack();
		node.setVisible(true);
	}
}
