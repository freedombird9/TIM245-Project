
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.joda.time.DateTime;

@SuppressWarnings("unused")
public class AmazonCategoryCrawler extends BaseCrawler implements GetID {
	
	/**
	 * Example (Tablet PCs):
	 * 	getIDsByNode getid = new getIDsByNode("541966%2C1232597011",1,300);
	 * @param thenode node (category) from Amazon
	 * @param fromPage the first page
	 * @param toPage the last page, each page has 5 products, so toPage = 300 will parse at most 1500 product IDs from the category
	 */
	public AmazonCategoryCrawler(String outDir, String thenode, int fromPage, int toPage){
		this.nodeid = thenode;
		this.from = fromPage;
		this.to = toPage;
		this.items = new ItemList();
		this.outDir = outDir;
	}

	public void updateCSV(String oldfile) throws MalformedURLException, IOException{
		ItemList oldlist = new ItemList(oldfile);
		oldlist.mergeList(items);
		oldlist.writeToCSV(oldfile);
	}
	//http://www.amazon.com/b?node=7740213011
	public void getIDList() throws IOException{
		for (int i = from; i <= to; i++) {
			String url = "http://www.amazon.com/s?ie=UTF8&page="+i+"&rh=n%3A"+nodeid;
			String thepage = readWebPage(url);
			DateTime dt = new DateTime();
			System.out.println(dt+ "Page "+i);

			String patternString = "(href=\"http://www.amazon.com/)(.*?)(/dp/)(\\S{10})(\")";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(thepage);
			while (matcher.find()) {
				items.addItem(matcher.group(4));
			}
		}
	}
	
	private Set<String> getItems(){
		return items.returnIDsAsSet();
	}
	/**
	 * Write all ASIN in the node to a csv file
	 * @param filePath The file path to write all ASIN in a node to
	 * @throws IOException 
	 */
	public void writeIDsToCSV(String filePath) throws IOException {
		items.writeToCSV(filePath);
	}
	
	
	public void goCrawl() throws IOException{
		File outDirFile = new File(outDir);
		if(!outDirFile.exists()){
			outDirFile.mkdirs();
		}
		getIDList();
		Set<String> ids = getItems();
	    for(String s:ids){
	    	System.out.println(s);
	    		
	    	String res = readWebPage("http://www.amazon.com/dp/"+s);
	    	PrintWriter out = new PrintWriter(outDir+s+".html");
	    	out.println(res);
	    	out.close();
	    }

	}

}
