package simpleWebCrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawlerRegex {

	public static void main(String[] args) {
		
		WebCrawlerRegex crawler = new WebCrawlerRegex();
		String urlRoot = "https://news.ycombinator.com/";
		crawler.crawl(urlRoot, 100);
		
	}
	
	
	private Queue<String> urlQueue;
	private ArrayList<String> urlVisited;
	
	
	public WebCrawlerRegex() {
		urlQueue = new LinkedList<>();
		urlVisited = new ArrayList<>();
	}
	
	
	public void crawl(String urlRoot, int breakpoint) {
		
		urlQueue.add(urlRoot);
		urlVisited.add(urlRoot);

		int urlNumber = 0;
		
		while(!urlQueue.isEmpty()) {
			
			String htmlRaw = "";
			
			try {
				
				// create url
				URL url = new URL(urlQueue.remove());
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine = in.readLine();
				
				// read each html line and concatenate it to htmlRaw
				while(inputLine != null) {
					
					htmlRaw += inputLine;
					inputLine = in.readLine();				
							
				}
				
				in.close();				
				
			} catch (Exception e) {
				e.printStackTrace();	
			}
			
			// create a regex for validating href
			String urlRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
			Pattern pattern = Pattern.compile(urlRegex);
			Matcher matcher = pattern.matcher(htmlRaw);
			
			// each regex match is added to the queue
			while (matcher.find()) {
				
				String theURL = matcher.group();
				
				if (!urlVisited.contains(theURL)) {
					
					urlNumber++;
					urlVisited.add(theURL);
					System.out.println(urlNumber + ". " + theURL);
					urlQueue.add(theURL);
					
				}
				if (urlVisited.size() == breakpoint+1) {
					break;
				}
				
			}
			
			// exit when we've visited 100 URLs
			if (urlVisited.size() == breakpoint+1) {
				break;
			}
			
		}
			
	}

}
