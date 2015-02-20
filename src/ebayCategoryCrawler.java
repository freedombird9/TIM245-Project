import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ebayCategoryCrawler extends BaseCrawler{

	private ArrayList<String> catUrls;
	
	
	// outDir is C:\Users\Administrator\Documents\TIM245\project\data\pages\ebay\
	public ebayCategoryCrawler(String outDir, ArrayList<String> catUrls){
		this.outDir = outDir;
		this.catUrls = catUrls;
		//this.items = new ItemList();
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
			String patternString = "(http://www.ebay.com/sch/)(.*?)(-/)(.*?)";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(catUrl);
			while(matcher.find()){
				// get the category name/sub-directory name under \ebay 
				String dirName = matcher.group(2);
				subDir = new File(outDir+dirName+"\\");
				//System.out.println(outDir+dirName+"\\");
				if(!subDir.exists()){
					subDir.mkdirs();
				}
			}
						
			Document doc = Jsoup.connect(catUrl).get();
			Elements products = doc.select("h3.lvtitle a[href]");
			for(Element link: products){
				String prodUrl = link.attr("href");
				//System.out.println(prodUrl);
				String prodPage = readWebPage(prodUrl);
				String prodPatternString = "(http://www.ebay.com/itm/)(.*?)(hash=)(.*)";
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
