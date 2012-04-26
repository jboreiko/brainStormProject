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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private QueryService queryService;
	private JPanel _suggestPanel;
	private JPanel _networkPanel;
	private JTextField _usernameField;
	private JTextField _ipField;
	
	public SuggestGUI() {
		super(new java.awt.BorderLayout());
		
		buildSuggestTab();
		buildNetworkTab();
		buildChatTab();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		ImageIcon wiki = new ImageIcon("./lib/question.jpeg");
		tabbedPane.addTab("Suggestions", wiki, _suggestPanel, "Get Suggestions");
		
		ImageIcon google = new ImageIcon("./lib/web.jpeg");
		tabbedPane.addTab("Networking", google, _networkPanel, "Set Up Networking");
		
		this.add(tabbedPane);
		
		queryService = new QueryService();
	}
	
	
	
	private void buildChatTab() {
		
	}



	private void buildNetworkTab() {
		_networkPanel = new JPanel();
		JPanel hostPanel = new JPanel();
		JPanel clientPanel = new JPanel();
		JPanel usernamePanel = new JPanel();
		JLabel usernamelabel = new JLabel("Username: ");
		_usernameField = new JTextField(15);
		_usernameField.setEditable(true);
		JButton beHostButton = new JButton("Host a Brainstorm");
		beHostButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_usernameField.getText().isEmpty()) {
					// no username specified
					JOptionPane.showMessageDialog(_networkPanel, "You must have a username.", "No Username", JOptionPane.ERROR_MESSAGE);
				}
				else {
					// call networking becomehost method
					//_usernameField.getText for parameter
					// check for bad connection boolean
				}
			}
		});
		
		JLabel ipLabel = new JLabel("IP Address: ");
		_ipField = new JTextField(25);
		_ipField.setEditable(true);
		
		JButton joinButton = new JButton("Join a Brainstorm");
		joinButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_ipField.getText().isEmpty()) {
					// no ip address specified
					JOptionPane.showMessageDialog(_networkPanel, "You must enter the host's IP address to connect to.", "Invalid IP Address", JOptionPane.ERROR_MESSAGE);
				} else if(_usernameField.getText().isEmpty()) {
					// no username specified
					JOptionPane.showMessageDialog(_networkPanel, "You must enter a username before connecting.", "No Username ", JOptionPane.ERROR_MESSAGE);
				}
				else {
					// call networking becomeclient method
					// _ipField.getText and _usernameField.getText for parameters
					// check for bad connection boolean
					//
					
				}
			}
		});
		
		usernamePanel.add(usernamelabel,BorderLayout.WEST);
		usernamePanel.add(_usernameField, BorderLayout.EAST);
		hostPanel.add(beHostButton, BorderLayout.CENTER);
		JPanel holderPanel = new JPanel();
		holderPanel.add(usernamePanel, BorderLayout.NORTH);
		holderPanel.add(hostPanel, BorderLayout.SOUTH);
		clientPanel.add(ipLabel, BorderLayout.WEST);
		clientPanel.add(_ipField, BorderLayout.CENTER);
		clientPanel.add(joinButton, BorderLayout.EAST);
		
		//_networkPanel.add(usernamePanel, BorderLayout.NORTH);
		//_networkPanel.add(hostPanel, BorderLayout.CENTER);
		_networkPanel.add(holderPanel, BorderLayout.CENTER);
		_networkPanel.add(clientPanel, BorderLayout.SOUTH);
		
	}



	private void buildSuggestTab() {
		_suggestPanel = new JPanel();
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
		wikiScrollPane.setPreferredSize(new Dimension(400, 250));
		wikiPanel.add(wikiScrollPane);
		
		JPanel dictPanel = new JPanel();
		dictoutput = new JTextArea(20, 50);
		dictoutput.setEditable(false);
		dictoutput.setLineWrap(true);
		dictoutput.setWrapStyleWord(true);
		JScrollPane dictScrollPane = new JScrollPane(dictoutput);
		dictScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dictScrollPane.setPreferredSize(new Dimension(400, 250));
		dictPanel.add(dictScrollPane);
		
		JPanel duckPanel = new JPanel();
		duckoutput = new JTextArea(20, 50);
		duckoutput.setEditable(false);
		duckoutput.setLineWrap(true);
		duckoutput.setWrapStyleWord(true);
		JScrollPane duckScrollPane = new JScrollPane(duckoutput);
		duckScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		duckScrollPane.setPreferredSize(new Dimension(400, 250));
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
		
		
		_suggestPanel.add(inputpanel, BorderLayout.PAGE_START);
		_suggestPanel.add(buttonPanel, BorderLayout.CENTER);
		_suggestPanel.add(tabbedPane, BorderLayout.PAGE_END);
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
