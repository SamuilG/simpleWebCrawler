package simpleWebCrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	
	private Queue<String> urlQueue;
	private Queue<String> urlVisited;
	
	public WebCrawler() {
		urlQueue = new LinkedList<>();
		urlVisited = new ArrayList<>();
		
	}
	
	public void crawl(String urlRoot, int breakpoint) {
		
		urlQueue.add(urlRoot);
		urlVisited.add(urlRoot);
		
		while(!urlQueue.isEmpty()) {
			
			String s = urlQueue.remove();
			String htmlRaw = "";
			
			try {
				
				// create url
				URL url = new URL(s);
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
			
			// create a regex for validating html content
//			String urlRegex = "^[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$";
//			String urlRegex = "(www|http:|https:)+[^\s]+[\\w]";
			Pattern pattern = Pattern.compile("^[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$");
			Matcher matcher = pattern.matcher(htmlRaw);
			
			// each regex match is added to the queue
			breakpoint = getBreakpoint(breakpoint, matcher);
			
			// exit on breakpoint
			if (breakpoint == 0) {
				break;
			}
			
		}
			
	}
	
	
	private int getBreakpoint(int breakpoint, Matcher matcher) {
		
		while (matcher.find()) {
			
			String theURL = matcher.group();
			
			if (!urlVisited.contains(theURL)) {
				
				urlVisited.add(theURL);
				System.out.println("Found URL: " + theURL);
				urlQueue.add(theURL);
				
			}
			
			if (breakpoint == 0) {
				break;
			}
			breakpoint--;
			
		}
		
		return breakpoint;
		
	}
	
	public static void main(String[] args) {
		
		WebCrawler crawler = new WebCrawler();
		String urlRoot = "https://news.ycombinator.com/";
		crawler.crawl(urlRoot, 100);
		
	}
	
	

}
