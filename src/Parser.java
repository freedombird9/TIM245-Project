import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.lang.model.element.Element;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;


public class Parser {

	public static void main(String[] args) throws IOException{
//		File input = new File("/Users/peijiang/tim245/169694643.html");
		File input = new File("/Users/peijiang/tim245/B0000A1ZMS.html");
//		File input = new File("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\coffe_makers\\B0000A1ZMS.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		final TitleHandler titleHandler = new TitleHandler();
		final PriceHandler priceHandler = new PriceHandler();
		final ImageHandler imageHandler = new ImageHandler();
		final HashMap <Integer, Features> titleFeatures = new HashMap <Integer, Features>();
		final HashMap <Integer, Features> priceFeatures = new HashMap <Integer, Features>();
		final HashMap <Integer, Features> imageFeatures = new HashMap <Integer, Features>();
		//final HtmlInfo info = new HtmlInfo();
		
		final HashMap <Node, Integer> idMap = new HashMap <Node, Integer>();
		doc.traverse(new NodeVisitor(){

			@Override
			public void head(Node node, int depth) {
				// TODO Auto-generated method stub
				int sequentialNodeId = idMap.size();
				//System.out.println("node hash "+ sequentialNodeId);
				idMap.put(node, sequentialNodeId);
				titleHandler.start(node, sequentialNodeId, titleFeatures);
				priceHandler.start(node,titleHandler,depth, priceFeatures);
				imageHandler.start(node, titleHandler, sequentialNodeId, depth, titleFeatures);
			}

			@Override
			public void tail(Node node, int depth) {
				// TODO Auto-generated method stub
				titleHandler.end(node, titleFeatures);
				
				Node hid = node.nodeName()=="#text"?node.parent():node;
				priceHandler.end(node,titleHandler, idMap.get(hid), depth, priceFeatures);
				imageHandler.end(node, titleHandler, idMap.get(hid) , depth, imageFeatures);
			}
			
		});
		try{
			outputCsv("/Users/peijiang/tim245/titles.csv", titleFeatures);
			outputCsv("/Users/peijiang/tim245/prices.csv", priceFeatures);
//			outputCsv("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\images.csv", imageFeatures);
//			outputCsv("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\prices.csv", priceFeatures);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void outputCsv(String outPath, HashMap <Integer, Features> features) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(outPath);
		boolean printAttrName = true;
		for( int recordId:features.keySet()){
			Features record = features.get(recordId);
			boolean printComma = false;
			if(printAttrName){
				for(String featureName : record.getKeys()){
					if(printComma){
						pw.print(",");	
					}else{
						printComma=true;
					}
					pw.print(featureName);
				}
				printAttrName = false;
			}
			pw.println();
			printComma = false;
			for(String featureName : record.getKeys()){
				if(printComma){
					pw.print(",");	
				}else{
					printComma=true;
				}
				if(featureName == "text" )
					pw.print("\""+record.getFeature(featureName)+"\"");
				else
					pw.print(record.getFeature(featureName));
			}
			pw.println();
		}
		pw.close();
	}
		
}