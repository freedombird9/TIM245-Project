import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;


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

		// Amazon
		//String categoryId = "353609011"; //tires
		//String categoryId = "3737671"; // ACs
		//String categoryId = "2611187011"; // men pants
		//String categoryId = "7740213011"; // coffe makers
		//String categoryId = "3418871"; // goggles
		//String categoryId = "6503737011"; // red wines
		//String categoryId = "7792274011"; // lotions
		//String categoryId = "599858"; // magazines
		//String categoryId = "11971251"; // guitars
		//String outDir = "/home/jp/crawler/"+categoryId+"/";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\tires\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\acs\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\pants\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\coffe_makers\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\goggles\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\red_wines\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\lotions\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\magazines\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\guitars\\";
		//AmazonCategoryCrawler crawler = new AmazonCategoryCrawler(outDir, categoryId, 1, 5);
		//crawler.goCrawl();
		
		
		// Alibaba
		//String categoryId = "100003070"; // men's clothing
		//String categoryId = "702"; // laptops
		//String categoryId = "660204"; // hair care
		//String categoryId = "66030103"; // hair dye
		//String categoryId = "100009100"; // LED lighting
		//String categoryId = "100009043"; // coffee beans
		//String categoryId = "380420"; // luggage
		//String categoryId = "1511"; // watches
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\men_clothing\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\laptops\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\hair_care\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\hair_dye\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\LED_lighting\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\coffee_beans\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\luggage\\";
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Alibaba\\watches\\";

		//AlibabaCategoryCrawler crawler = new AlibabaCategoryCrawler(outDir, categoryId, 1, 5);
		//crawler.goCrawl();
		
		
		// ebay
		// user provide the directory path and the urls of the categories needed to be crawled
		
		//String catUrl1 = "http://www.ebay.com/sch/Air-Guns-Slingshots-/178886/i.html?_ipg=200&rt=nc";
		//String catUrl2 = "http://www.ebay.com/sch/Computers-Tablets-Networking-/58058/i.html?_from=R40&LH_BIN=1&LH_ItemCondition=3&_fss=1&LH_SellerWithStore=1&_nkw=lenovo&_ipg=200&rt=nc";
		//String catUrl1 = "http://www.ebay.com/sch/Vases-/37933/i.html?_ipg=200&rt=nc";
		//String catUrl2 = "http://www.ebay.com/sch/DVDs-Bluray-Discs-/617/i.html?_ipg=200&rt=nc";
		//String catUrl3 = "http://www.ebay.com/sch/Flea-Tick-Remedies-/20749/i.html?_ipg=200&rt=nc";
		
		//String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\ebay\\";
		//ArrayList<String> urls = new ArrayList<>(Arrays.asList(catUrl1, catUrl2, catUrl3));
		
		//ebayCategoryCrawler crawler = new ebayCategoryCrawler(outDir, urls);
		//crawler.goCrawl();
		
		
		// Walmart
		String catUrl1 = "http://www.walmart.com/browse/movies-tv/children-family/4096_616859_617001";
		String outDir = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\walmart\\";

		ArrayList<String> urls = new ArrayList<>(Arrays.asList(catUrl1));
		WalmartCategoryCrawler crawler = new WalmartCategoryCrawler(outDir, urls, 1, 5);
		crawler.goCrawl();
 		
	}

}
