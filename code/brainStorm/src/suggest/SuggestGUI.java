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
		
		//private final JdomParser JDOM_PARSER = new JdomParser();
		
		public void actionPerformed(ActionEvent e) {
			
			String query = input.getText();
			
			List<Future<List<String>>> futures = new ArrayList<Future<List<String>>>();
			//futures.add(queryService.submit(query, 4));
			for (int i = 0; i < 3; i++) {
				futures.add(i, queryService.submit(query, i));
			}
			int j =0;
			for (Future<List<String>> future:futures) {
				try {
					System.out.println("NUMBERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR:         " + j);
					String result = future.get().get(0);
					if (j == 0) {
						//result = future.get().get(0);
						int indexOf = result.indexOf("'''");
						System.out.println(indexOf);
						int indexOf2 = result.indexOf("==", indexOf);
						System.out.println(indexOf2);
						String substring = "";
						if (indexOf < 0 || indexOf2 < 0) {
							substring = result;
						}
						else {
							substring = result.substring(indexOf, indexOf2);
						}
						wikioutput.setText(substring);
						System.out.println(substring);
					}
					else if (j==1) {
						//result = future.get().get(0);
						int indexOf = result.indexOf("webDefinitions");
						System.out.println(indexOf);
						int indexOf2 = result.indexOf("entries", indexOf);
						System.out.println(indexOf2);
						indexOf = result.indexOf("text", indexOf2);
						System.out.println(indexOf);
						indexOf = result.indexOf("text", indexOf+1);
						System.out.println(indexOf);
						indexOf2 = result.indexOf("language", indexOf);
						System.out.println(indexOf2);
						String substring = "";
						if (indexOf < 0 || indexOf2 < 0) {
							substring = result;
						}
						else {
							substring = result.substring(indexOf+5, indexOf2);
						}
						dictoutput.setText(substring);
						System.out.println(substring);
						System.out.println(result);
					}
					else if (j==2) {
						//future.get();
						int indexOf = result.indexOf("Definition");
						System.out.println(indexOf);
						int indexOf2 = result.indexOf("DefinitionS", indexOf);
						System.out.println(indexOf2);
						String substring = "";
						if (indexOf < 0 || indexOf2 < 0) {
							substring = result;
						}
						else {
							substring = result.substring(indexOf, indexOf2);
						}
						duckoutput.setText(substring);
						System.out.println(substring);
						System.out.println(result);
					}
					//JsonRootNode json = JDOM_PARSER.parse(future.get());
					//json.
					//System.out.println(json.getElements());
					j++;
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} //catch (InvalidSyntaxException e2) {
					// TODO Auto-generated catch block
					//e2.printStackTrace();
				//}
			}
//			List<String> response = new ArrayList<String>();
//			//String requestUrl = "http://www.urbandictionary.com/define.php?term=" + query;
//			//String requestUrl = "http://www.stands4.com/services/v1/defs.aspx?tokenid=tk324324&word=" +query;
//			//String requestUrl = "http://api.duckduckgo.com/?format=json&pretty=1&q=" + query;
//			//String requestUrl = "http://en.wikipedia.org/w/api.php?format=json&action=query&titles=Java&prop=revisions&rvprop=content";
//			//String requestUrl = "http://www.google.com/dictionary/json?callback=dict_api.callbacks.id100&q=consistent&sl=en&tl=en&restrict=pr%2Cde&client=te";
//			String requestUrl = "http://search.twitter.com/search.json?q=" + query;
//			//String requestUrl = "http://www.google.co.in/#hl=en&source=hp&q=java&btnG=Google+Search&m eta=&aq=f&oq"+query+"&fp=c5b9ba6cbe6cba1e";
//			//String requestUrl = "http://www.google.com/search?q=" + query;
//			//String requestUrl = "http://us.yhs4.search.yahoo.com/yhs/search?p=" + query;
//			//String requestUrl = "http://www.askjeeves.com/main/askjeeves.asp?ask=" + query;
//			
//			URL url;
//			try {
//				url = new URL(requestUrl);
//				URLConnection urlConn = url.openConnection();
//				urlConn.setDoOutput(false);
//				
//				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//				
//		        String line = "";
//		        while ((line = reader.readLine()) != null) {
//		        	System.out.println(line);
//		            response.add(line);
//		        }
//		        
//		        reader.close();
//		        //String[] strings = (String[]) response.toArray();
//		        //System.out.println("IN ARRAY:  " + strings[1]);
//			//System.out.println(query);
//			//output.setText(query);
//			} catch (MalformedURLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
		}
		
	}
}
