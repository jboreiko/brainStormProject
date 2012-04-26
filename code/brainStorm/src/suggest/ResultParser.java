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
					// trying to fix <*> tags
//					if (text.contains("<")) {
//						System.out.println("inside");
//						int indexOf = text.indexOf("<");
//						int indexOf2 = text.indexOf(">");
//						System.out.println("un" + indexOf);
//						System.out.println("du" + indexOf2);
//						text= text.substring(0,indexOf) + text.substring(indexOf2, text.length());
//						text.replaceAll("<[^>]*>", "");
//						System.out.println(text);
//					}
					line += text;
					line += "\n\n";
					System.out.println("aline:   " + line);
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
				
				// trying to fix <*> tags
//				if (text.contains("<")) {
//					int indexOf = text.indexOf("<");
//					int indexOf2 = text.indexOf(">");
//					text.replaceAll("\\x3[^\\x3]*\\x3e", "");
//					System.out.println(text);
//				}
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
	    }
	    
	    JSONObject pageidObj = pagesObj.getJSONObject(substring);
	    JSONArray revisionArr = pageidObj.getJSONArray("revisions");
	    JSONObject revisionObj = revisionArr.getJSONObject(0);
	    String article = revisionObj.getString("*");
	    
	    // TODO handle multiple options
	    //System.out.println(article.substring(0, (int) article.length()/2));
	    
	    // TODO handle parsing
	    //done briefly below
//			int indexOf = response.indexOf("'''");
//			System.out.println(indexOf);
//			int indexOf2 = response.indexOf("==", indexOf);
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
	    
		return article;
	}
	
	

}
