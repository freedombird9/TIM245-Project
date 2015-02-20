import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WalmartCategoryCrawler extends BaseCrawler{

	private ArrayList<String> catUrls;
	
	
	public WalmartCategoryCrawler(String outDir, ArrayList<String> urls, int fromPage, int toPage){
		this.outDir = outDir;
		this.from = fromPage;
		this.to = toPage;
		this.catUrls = urls;
	}
	
	
	@Override
	protected String readWebPage(String weburl) throws IOException{
		URL url = new URL(weburl);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		return body;
	}
	
	
	
	
	@Override
	void goCrawl() throws IOException {
		File outDirFile = new File(outDir);
		if(!outDirFile.exists()){
			outDirFile.mkdirs();
		}
		
		// fetch each CATEGORY'S url
		for (String catUrl: catUrls){
			File subDir = new File(outDir+"default\\");
			for (int i=from; i<=to; ++i){
				String pageUrl = catUrl+"?page="+i;
				//System.out.println(pageUrl);
				String patternString = "(http://www.walmart.com/browse/)(.*)(/\\d+)";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(catUrl);
				
				while(matcher.find()){
					// get the category name/sub-directory name under \walmart 
					String dirName = matcher.group(2);
					subDir = new File(outDir+dirName+"\\");
					//System.out.println(outDir+dirName+"\\");
					if(!subDir.exists()){
						subDir.mkdirs();
					}
				}
						
				Document doc = Jsoup.connect(pageUrl).get();
				Elements products = doc.select("a.js-product-title");
				for(Element link: products){
					String prodUrl = link.attr("href");
					//System.out.println(prodUrl);
					prodUrl = "http://www.walmart.com" + prodUrl;
					System.out.println(prodUrl);
					String prodPage = readWebPage(prodUrl);
					String prodPatternString = "(/ip/)(.*?)(/)(\\d+)";
					Pattern prodPattern = Pattern.compile(prodPatternString);
					Matcher prodMatcher = prodPattern.matcher(prodUrl);
					while(prodMatcher.find()){
						String pid = prodMatcher.group(4);
						PrintWriter out = new PrintWriter(subDir+"\\"+pid+".html");
						//System.out.println(subDir+pid+".html");
						out.println(prodPage);
						out.close();
					}
				}				
			}	
		}
	}
}
