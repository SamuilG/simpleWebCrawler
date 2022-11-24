package simpleWebCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebCrawlerJsoup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner inputURL = new Scanner(System.in);  // accept input
	    System.out.println("Enter starting/root URL:");
	    
	    String URL = inputURL.nextLine();
	    inputURL.close();
		
//		String URL = "https://news.ycombinator.com";
		craw(URL, new ArrayList<String>());
		
	}
	
	private static void craw(String URL, ArrayList<String> visitedURLs) {
		
		if (visitedURLs.size() < 100) {	// stop when we've visited 100 URLs
			Document doc = request(URL, visitedURLs);
			
			if (doc != null) {
				for (Element link : doc.select("a[href]")) {
					String nextLink = link.absUrl("href");
					if (visitedURLs.contains(nextLink) == false) {
						craw(nextLink, visitedURLs);
					}
				}
			}
		}
		
		
		
	}
	
	private static Document request(String URL, ArrayList<String> visitedURLs) {
		try {
			
			Connection conn = Jsoup.connect(URL);
			Document doc = conn.get();
			
			if (conn.response().statusCode() == 200) {
				int numberURL = visitedURLs.size();
				numberURL++;
				System.out.println(numberURL + ". " + URL);
				visitedURLs.add(URL);
				return doc;
			}
			return null;
			
		} catch (IOException e) {
			return null;
		}
	}

}
