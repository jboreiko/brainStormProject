package suggest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ResultParser {
	
	
	public ResultParser() {
		
	}

	public String parse(String response, int number) {
		System.out.println("NUMBERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR:         " + number);
		if (number == 0) {
			System.out.println(response);
			return wikiParse(response);
			
		}
		else if (number==1) {
			System.out.println("dict:   " + response);
			return dictParse(response);
		}
		else if (number==2) {
			System.out.println("ducky:  " + response);
			return duckParse(response);
//			int indexOf = response.indexOf("Definition");
//			System.out.println(indexOf);
//			int indexOf2 = response.indexOf("DefinitionS", indexOf);
//			System.out.println(indexOf2);
//			String substring = "";
//			if (indexOf < 0 || indexOf2 < 0) {
//				substring = response;
//			}
//			else {
//				substring = response.substring(indexOf, indexOf2);
//			}
//			System.out.println(substring);
//			return substring;
		}
		
		return "No Results Returned.";
	}

	private String dictParse(String response) {
		response = response.substring(25, response.length()-10);
		System.out.println("|" + response + "|");
		
		JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
		String querystr = json.getString("query");
		System.out.println("dictquery:  " + querystr);
		
		// Primaries Definitions
		if (!json.has("primaries")) {
			// no results
			return "No Definitions Available";
		}
		JSONArray primariesArr = json.getJSONArray("primaries");
		String def = "Definition Results:\n";
		String line = "";
		int numTotal = 1;
		for (int i = 0; i < primariesArr.size(); i++) {
			JSONObject topObj = primariesArr.getJSONObject(i);
			JSONArray objEntriesArr = topObj.getJSONArray("entries");
			for (int j = 0; j < objEntriesArr.size(); j++) {
				JSONObject typeObj = objEntriesArr.getJSONObject(j);
				if (!typeObj.getString("type").equals("meaning")) {
					System.out.println("skipping:  " + typeObj.getString("type"));
					continue;
				}
				JSONArray termArr = typeObj.getJSONArray("terms");
				for (int k = 0; k < termArr.size(); k++) {
					line = "";
					line += numTotal + ".   ";
					JSONObject textObj = termArr.getJSONObject(k);
					String text = textObj.getString("text");
					// remove <*> tags
					while (text.contains("<")) {
						int indexOf = text.indexOf("<");
						int indexOf2 = text.indexOf(">");
						text= text.substring(0,indexOf) + text.substring(indexOf2+1, text.length());
					}
					line += text;
					line += "\n\n";
					def += line;
					numTotal++;
				}
			}
		}
		System.out.println("totsdef" + def);
		return def;
		
	}

	private String duckParse(String response) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
		String results = "                    Related Categories and Topics : \n";
		JSONArray relatedArr = json.getJSONArray("RelatedTopics");
		if (relatedArr.size() == 0) {
			results += "No Topics Found.";
			return results;
		}
		String line = "";
		for (int i = 0; i < relatedArr.size(); i++) {
			JSONObject topicObj = relatedArr.getJSONObject(i);
			if (!topicObj.has("Topics")) {
				continue;
			}
			JSONArray topicsArr = topicObj.getJSONArray("Topics");
			String name = topicObj.getString("Name");
			results += name + ":\n";
			int numper = 1;
			for (int j = 0; j < topicsArr.size(); j++) {
				line = "";
				line += numper + ".   ";
				JSONObject elementObj = topicsArr.getJSONObject(j);
				String text = elementObj.getString("Text");
				
				line += text;
				line += "\n\n";
				System.out.println("aline:   " + line);
				results += line;
				numper++;
			}
			
		}
		System.out.println("totsduck" + results);
		return results;
	}
	

	private String wikiParse(String response) {
	    JSONObject json = (JSONObject) JSONSerializer.toJSON(response);
	    JSONObject queryObj = json.getJSONObject("query");
	    JSONObject pagesObj = queryObj.getJSONObject("pages");
	    
	    int indexOf = response.indexOf("pages");
		System.out.println("in1  " + indexOf);
		int indexOf2 = response.indexOf("{", indexOf);
		System.out.println("in2  " + indexOf2);
		indexOf = response.indexOf(":", indexOf2);
		System.out.println("in3  " + indexOf);
		String substring = "";
		if (indexOf < 0 || indexOf2 < 0) {
			substring = response;
		}
		else {
			substring = response.substring(indexOf2+2, indexOf-1);
		}
		System.out.println("pageId" + substring);
	    if (substring.equals("-1")) {
	    	//TODO handle no page results from wiki
	    	// return/break out of parsing
	    	return("No Wikipedia Results Found");
	    }
	    
	    JSONObject pageidObj = pagesObj.getJSONObject(substring);
	    JSONArray revisionArr = pageidObj.getJSONArray("revisions");
	    JSONObject revisionObj = revisionArr.getJSONObject(0);
	    String article = revisionObj.getString("*");
	    
	    // TODO handle redirects
	    if (article.startsWith("#REDIRECT")) {
	    	return article;
	    }
	    
	    article = removeBraces(article);
	    article = removeParens(article);
	    while (article.startsWith("\n")) {
	    	article = article.substring(1, article.length());
	    }
	    article = removeTriple(article);
	    article = removeTags(article);
	    article  = removeTagBraces(article);
	    
	    // TODO handle multiple options
	    if (article.indexOf(":") < article.indexOf("\n")) {
	    	System.out.println("Multiple Options son");
	    	return article;
	    }
	    
	    indexOf = article.indexOf("==");
	    article = article.substring(0, indexOf);
	    
	    
//	    substring = "";
//	    int index = article.indexOf("'''");
//	    System.out.println(index);
//	    
//	    
//	    
//	    // TODO handle parsing
//	    //done briefly below
//	    int index2 = article.indexOf("==", index);
//	    System.out.println(index2);
//	    if (index < 0 || index2 < 0) {
//	    	System.out.println("WHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHYYYYYYYYYYYYYYYYYYYYYYYYY");
//	    	System.out.println(article);
//	    	System.out.println("WHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHYYYYYYYYYYYYYYYYYYYYYYYYY");
//	    }
//	    else {
//	    	article = article.substring(index, index2);
//	    }
	    //remove <*> tags
//	    while (text.contains("<")) {
//	    	int indexOf = text.indexOf("<");
//	    	int indexOf2 = text.indexOf(">");
//	    	text= text.substring(0,indexOf) + text.substring(indexOf2+1, text.length());
//	    }
	    
		return article;
	}
	
	private String removeTagBraces(String article) {
		int balance = 0;
		int start = 0;
		int end = 0;
		boolean done = false;
		for (int i = 0; i < article.length(); i++) {
			char letter = article.charAt(i);
			if(letter == '<') {
				balance++;
				if (balance == 1) {
					start = i;
				}
			}
			else if (letter == '>') {
				balance--;
				if (balance == 0) {
					end = i;
					done = true;
				}
			}
			if (done) {
				i = i - end + start - 1;
				article = article.substring(0,start) + article.substring(end+1, article.length());
				done = false;
			}
		}
		return article;
	}
	
	private String removeTags(String article) {
		int index = article.indexOf("<ref");
		int index2 = article.indexOf("/ref>", index);
		while (index != -1 && index2 != -1) {
			article = article.substring(0,index) + article.substring(index2+5, article.length());
			index = article.indexOf("<ref");
			index2 = article.indexOf("/ref>", index);
		}
		return article;
	}

	private String removeTriple(String article) {
		int index = article.indexOf("'''");
		while (index != -1) {
			article = article.substring(0,index) + article.substring(index+3, article.length());
			index = article.indexOf("'''");
		}
		return article;
	}

	private String removeParens(String article) {
		int balance = 0;
		int start = 0;
		int end = 0;
		boolean done = false;
		for (int i = 0; i < article.length(); i++) {
			char letter = article.charAt(i);
			if(letter == '(') {
				balance++;
				if (balance == 1) {
					start = i;
				}
			}
			else if (letter == ')') {
				balance--;
				if (balance == 0) {
					end = i;
					done = true;
				}
			}
			if (done) {
				i = i - end + start - 1;
				article = article.substring(0,start) + article.substring(end+1, article.length());
				done = false;
			}
		}
		return article;
	}
	
	private String removeBraces(String article) {
		int balance = 0;
		int start = 0;
		int end = 0;
		boolean done = false;
		for (int i = 0; i < article.length(); i++) {
			char letter = article.charAt(i);
			if(letter == '{') {
				balance++;
				if (balance == 1) {
					start = i;
				}
			}
			else if (letter == '}') {
				balance--;
				if (balance == 0) {
					end = i;
					done = true;
				}
			}
			if (done) {
				i = i - end + start - 1;
				article = article.substring(0,start) + article.substring(end+1, article.length());
				done = false;
			}
		}
		return article;
	}
	
	

}
