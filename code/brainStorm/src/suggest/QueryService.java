package suggest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryService {
	
	//private LinkedBlockingQueue<String> workQueue;
	private ExecutorService workers = Executors.newFixedThreadPool(5);
	private ResultParser _parser;

	public QueryService() {
		//workQueue = blockingQueue;
		_parser = new ResultParser();
	}
	
	public Future<String> submit(String query, int i) {
		
		return workers.submit(new Query(query, i));
	}
	
	private class Query implements Callable<String> {
		private String _query;
		private int _number;
		
		public Query(String query, int i) {
			try {
				this._query = URLEncoder.encode(query, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			this._number = i;
		}

		@Override
		public String call() throws Exception {
			String requestUrl = "";
			switch(_number) {
			case 0:
				requestUrl = "http://en.wikipedia.org/w/api.php?format=json&action=query&titles=" + _query + "&prop=revisions&rvprop=content";
				break;
			case 1:
				requestUrl = "http://www.google.com/dictionary/json?callback=dict_api.callbacks.id100&q=" + _query + "&sl=en&tl=en&restrict=pr%2Cde&client=te";
				break;
			case 2:
				requestUrl = "http://api.duckduckgo.com/?format=json&pretty=1&q=" + _query;
				break;
//			case 3:
//				requestUrl = "http://us.yhs4.search.yahoo.com/yhs/search?p=" + _query;
//				break;
//			case 4:
//				requestUrl = "http://www.askjeeves.com/main/askjeeves.asp?ask=" + _query;
//				break;
			case 3:
				requestUrl = "http://search.twitter.com/search.json?q=" + _query;
				break;
//			case 4:
//				requestUrl = "http://www.urbandictionary.com/define.php?term=" + _query;
//				break;
			default:
				System.out.println("Number submitted is not supported");
				break;
				
			}
			String response = "";
			//String requestUrl = "http://www.google.co.in/#hl=en&source=hp&q=java&btnG=Google+Search&m eta=&aq=f&oq"+query+"&fp=c5b9ba6cbe6cba1e";
			//String requestUrl = "http://www.google.com/search?q=" + query;
			
			URL url;
			BufferedReader reader = null;
			String result = "No Results Returned.";
			try {
				url = new URL(requestUrl);
				URLConnection urlConn = url.openConnection();
				urlConn.setDoOutput(false);
				
				reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		        String line = "";
		        if (_number > 1) {
		        	while ((line = reader.readLine()) != null) {
		        		response += line;
		        	}
		        }
		        else {
		        	response = reader.readLine();
		        }
		        result = _parser.parse(response, _number);

			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			finally {
				reader.close();
			}
			return result;
		}
		
	}
	
	
	
}
