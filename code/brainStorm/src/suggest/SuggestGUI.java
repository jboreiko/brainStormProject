package suggest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SuggestGUI extends JPanel {
	private JTextField input;
	private JTextArea wikioutput;
	private JTextArea duckoutput;
	private JTextArea dictoutput;
	//private LinkedBlockingQueue<String> workQueue;
	private QueryService queryService;
	
	
	public SuggestGUI() {
		super(new java.awt.BorderLayout());
		
		//JPanel searchandresults = new JPanel(new GridLayout(3, 1));
		
		JPanel inputpanel = new JPanel();
		input = new JTextField(25);
		input.setEditable(true);
		inputpanel.add(input);
		
		JPanel buttonPanel = new JPanel();
		JButton suggestButton = new JButton("Suggestions?");
		suggestButton.addActionListener(new SuggestionListener());
		buttonPanel.add(suggestButton);
		
		JPanel wikiPanel = new JPanel();
		wikioutput = new JTextArea(20, 50);
		wikioutput.setEditable(false);
		wikioutput.setLineWrap(true);
		wikioutput.setWrapStyleWord(true);
		JScrollPane wikiScrollPane = new JScrollPane(wikioutput);
		wikiScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		wikiScrollPane.setPreferredSize(new Dimension(340, 600));
		wikiPanel.add(wikiScrollPane);
		
		JPanel dictPanel = new JPanel();
		dictoutput = new JTextArea(20, 50);
		dictoutput.setEditable(false);
		dictoutput.setLineWrap(true);
		dictoutput.setWrapStyleWord(true);
		JScrollPane dictScrollPane = new JScrollPane(dictoutput);
		dictScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dictScrollPane.setPreferredSize(new Dimension(340, 600));
		dictPanel.add(dictScrollPane);
		
		JPanel duckPanel = new JPanel();
		duckoutput = new JTextArea(20, 50);
		duckoutput.setEditable(false);
		duckoutput.setLineWrap(true);
		duckoutput.setWrapStyleWord(true);
		JScrollPane duckScrollPane = new JScrollPane(duckoutput);
		duckScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		duckScrollPane.setPreferredSize(new Dimension(340, 600));
		duckPanel.add(duckScrollPane);
		
		JTabbedPane tabbedPane = new JTabbedPane();

		ImageIcon wiki = new ImageIcon("./lib/wiki.jpeg");
		tabbedPane.addTab("Wikipedia", wiki, wikiPanel,
		                  "Wikipedia");

		ImageIcon google = new ImageIcon("./lib/dict.jpeg");
		tabbedPane.addTab("Dictionary", google, dictPanel,
		                  "Google Dictionary");

		ImageIcon duck = new ImageIcon("./lib/duckduck.jpeg");
		tabbedPane.addTab("DuckDuckGo", duck, duckPanel,
		                  "DuckDuckGo");

		
//		searchandresults.add(inputpanel);
//		searchandresults.add(buttonPanel);
//		searchandresults.add(outputPanel);
		this.add(inputpanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		this.add(tabbedPane, BorderLayout.SOUTH);
		//this.add(outputPanel, BorderLayout.SOUTH);
		
		//workQueue = new LinkedBlockingQueue<String>();
		queryService = new QueryService();
		
		
//		this.add(searchandresults, BorderLayout.CENTER);
	}
	
	
	
	private class SuggestionListener implements ActionListener {
		
		
		public void actionPerformed(ActionEvent e) {
			
			String query = input.getText();
			
			suggestThread suggestThread = new suggestThread(query);
			suggestThread.start();
			
		}
		
		private class suggestThread extends Thread {
			
			private String _query;
			
			public suggestThread(String query) {
				this._query = query;
			}

			@Override
			public void run() {
				List<Future<String>> futures = new ArrayList<Future<String>>();
				//futures.add(queryService.submit(query, 4));
				for (int i = 0; i < 3; i++) {
					futures.add(i, queryService.submit(_query, i));
				}
				int j =0;
				for (Future<String> future:futures) {
					try {
						String result = future.get();
						//System.out.println("NUMBERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR:  " + j);
						if (j == 0) {
							//System.out.println(result);
							wikioutput.setText(result);
						}
						else if (j==1) {
							//System.out.println(result);
							dictoutput.setText(result);
						}
						else if (j==2) {
							//System.out.println(result);
							duckoutput.setText(result);
						}
						j++;
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (ExecutionException e3) {
						e3.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
}
