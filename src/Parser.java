import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		//File input = new File("/Users/peijiang/tim245/B0000A1ZMS.html");
//		File input = new File("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\coffe_makers\\B0000A1ZMS.html");
//		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
//		File input = new File("/Users/peijiang/tim245/B0000A1ZMS.html");
//		File input = new File("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon\\coffe_makers\\B0000YWF5E.html");

		String website = "Amazon";
		
		File input = new File("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\"+website);

//		File input = new File("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\pages\\Amazon");


		final HashMap <Integer, Features> titleFeatures = new HashMap <Integer, Features>();
		final HashMap <Integer, Features> priceFeatures = new HashMap <Integer, Features>();
		final HashMap <Integer, Features> imageFeatures = new HashMap <Integer, Features>();
		final HashMap <Node, Integer> idMap = new HashMap <Node, Integer>();
		ArrayList <String> files = new ArrayList<String>();
		getFiles(input, files);
		int i=0;
		for(String file: files){		
			final HtmlInfo info = new HtmlInfo();
			//System.out.println(file + "  " + i++);
			Document doc = Jsoup.parse(new File(file), "UTF-8", "http://example.com/");
			//first pass, just to compute the tree size
			doc.traverse(new NodeVisitor(){
				@Override
				public void head(Node arg0, int arg1) {
					// TODO Auto-generated method stub
					info.sizeOfTree++;
				}

				@Override
				public void tail(Node arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				
			});
			final TitleHandler titleHandler = new TitleHandler(info.sizeOfTree);
			final PriceHandler priceHandler = new PriceHandler();
			final ImageHandler imageHandler = new ImageHandler(website);
			doc.traverse(new NodeVisitor(){
				@Override
				public void head(Node node, int depth) {
					// TODO Auto-generated method stub
					int sequentialNodeId = info.sequentialId++;//idMap.size();
					idMap.put(node, sequentialNodeId);
					titleHandler.start(node, sequentialNodeId, titleFeatures);
				//	priceHandler.start(node,titleHandler,depth, priceFeatures);
					imageHandler.start(node, titleHandler, sequentialNodeId, depth, imageFeatures);
				}

				@Override
				public void tail(Node node, int depth) {
					// TODO Auto-generated method stub
					titleHandler.end(node, titleFeatures);					
					Node hid = node.nodeName()=="#text"?node.parent():node;
				//	priceHandler.end(node,titleHandler, idMap.get(hid), depth, priceFeatures);
					imageHandler.end(node, titleHandler, idMap.get(hid), depth, imageFeatures);
				}
			});
			try{
//				outputCsv("/Users/peijiang/tim245/titles.csv", titleFeatures);
//				outputCsv("/Users/peijiang/tim245/prices.csv", priceFeatures);
				outputCsv("C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\"+website+"_images.csv", imageFeatures, i++);

				//outputCsv("/Users/peijiang/tim245/prices.csv", priceFeatures,i++);
				imageFeatures.clear();
				titleFeatures.clear();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("Number of files: " + i);		
	}
	
/*	@SafeVarargs
	private static void mergeFeatures(int nodeId, HashMap <Integer, Features> allMap, Features... features){
		
		Features allFeature = new Features();
		//HashMap <Integer, Features> allMap = new HashMap <Integer, Features>();
		
		for (Features feature: features){
			if(feature == null)
				continue;
			allFeature.putAll(feature);
		}
		
		allMap.put(nodeId, allFeature);	
	} */
	
	public static void getFiles(File node, ArrayList<String> filePaths){
 
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				getFiles(new File(node, filename), filePaths);
			}
		} else{
			if(node.getName().endsWith(".html"))
				filePaths.add(node.getAbsolutePath());
		}
 
	}
	
	
	private static void outputCsv(String outPath, HashMap <Integer, Features> features, int i) throws IOException{
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outPath, true)));
		boolean printAttrName;
		
		// print attribute name only for the first time
		if (i == 0)
			printAttrName = true;
		else 
			printAttrName = false;
		
	//	System.out.println(features.size());
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
				pw.println();
			}
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