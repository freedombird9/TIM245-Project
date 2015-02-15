import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class Crawler {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, ParseException,
			ClassNotFoundException, SQLException, InvalidKeyException,
			NoSuchAlgorithmException, InterruptedException {

		String categoryId = "353609011";
		String outDir = "/home/jp/crawler/"+categoryId+"/";
		File outDirFile = new File(outDir);
		if(!outDirFile.exists()){
			outDirFile.mkdirs();
		}
		AmazonCategoryCrawler category = new AmazonCategoryCrawler(categoryId, 1, 3);
	    category.getIDList();
	    Set<String> ids = category.getItems();
	    	for(String s:ids){
	    		System.out.println(s);
	    		HttpClient httpclient = new DefaultHttpClient();
	    		HttpGet httpget = new HttpGet("http://www.amazon.com/dp/"+s);
	    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
	    		String res = httpclient.execute(httpget, responseHandler);
	    		httpclient.getConnectionManager().shutdown();
	    		PrintWriter out = new PrintWriter(outDir+s+".html");
	    		out.println(res);
	    		out.close();
	    	}
		}

}
