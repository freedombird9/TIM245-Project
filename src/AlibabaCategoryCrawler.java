
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AlibabaCategoryCrawler extends BaseCrawler{
	
	public AlibabaCategoryCrawler(String outDir, String catId, int fromPage, int toPage){
		this.outDir = outDir;
		this.nodeid = catId;
		this.from = fromPage;
		this.to = toPage;
		this.items = new ItemList();
	}
	
	private void getItemList() throws IOException{
		for (int i=from; i<=to; ++i){
			String catUrl = "http://www.alibaba.com/catalogs/products/CID"+nodeid+"/"+i;
			Document doc = Jsoup.connect(catUrl).get();
			Elements products = doc.select("div.f-icon.m-item h2.title a[href]");
			for(Element prodUrl: products){
				items.addItem(prodUrl.attr("href"));
			}
			
		}
		
	}
	
	public void goCrawl() throws IOException{
		File outDirFile = new File(outDir);
		if(!outDirFile.exists()){
			outDirFile.mkdirs();
		}
		
		getItemList();
		Set<String> urls = items.returnIDsAsSet();
		String patternString = "(.*?)(_)(\\d+)(.*?)";
		for (String url: urls){
			String res = readWebPage(url);
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(url);
			//System.out.println(url);
			while (matcher.find()) {
				String pid = matcher.group(3);
				System.out.println(pid);
				PrintWriter out = new PrintWriter(outDir+pid+".html");
				out.println(res);
				out.close();
			}

		}

	}
}

