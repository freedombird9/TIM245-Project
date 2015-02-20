import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


abstract class BaseCrawler {
	
	protected ItemList items;
	protected String nodeid;
	protected int from;
	protected int to;
	protected String outDir;
	
	public void BaseeCrawler(){
		
	}
	
	abstract void goCrawl() throws IOException;

	protected String readWebPage(String weburl) throws IOException{
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet httpget = new HttpGet(weburl); 
		ResponseHandler<String> responseHandler = new BasicResponseHandler();    
		String responseBody = httpclient.execute(httpget, responseHandler);
		// responseBody now contains the contents of the page
		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}
}
