import java.util.HashMap;

import org.jsoup.nodes.Node;


public class ImageHandler extends FieldHandler{
	private boolean isImage = false;
	private boolean imageItemprop = false;
	private double altTitleSimiScore = -1; // default value, regarded as missing/NaN
	private boolean inList = false;
	private boolean inTable = false;
	private boolean hasScaling = false;
	private boolean isTerminal = false;
	private int hashId = 0;
	private int depth = -1;
	
	
	private int liCounter=0;
	private int tableCounter=0;
	
	public void start(Node node, TitleHandler titleHandler, int nodeId, int depth, HashMap<Integer, Features> records){
		
		String ics="";
		String text="";
	
		//System.out.println("node name: " + node.nodeName() );
		
		if(!titleHandler.inBody)
			return;
		if(node.nodeName() == "img")
			this.isImage = true;
		else this.isImage = false;
		
		if(node.attr("src").indexOf("http://ecx.images-amazon.com/images/I/31srOobQUJL._SY300_.jpg")>=0)
			System.out.println("sequential ID: "+nodeId);
		
		String itemprop = node.attr("itemprop")==null?"":node.attr("itemprop").toLowerCase();
		this.depth = depth - 1;
		
		if(itemprop != null && itemprop.indexOf("image")>=0)
			this.imageItemprop = true;
		else this.imageItemprop = false;
		if(node.nodeName() == "li")
			this.liCounter++;
		if(node.nodeName() == "table")
			this.tableCounter++;
		if(node.attr("width").length()>0 || node.attr("height").length()>0)
			this.hasScaling = true;
		else this.hasScaling = false;
		
		if(this.liCounter > 0)
			this.inList = true;
		else this.inList = false;
		
		if(this.tableCounter > 0)
			this.inTable = true;
		else this.inTable = false;
		
		if(node.attr("alt").length() > 0){
			text = node.attr("alt").toLowerCase();
			ics = lcs(text, titleHandler.htmlTitle);
			altTitleSimiScore = ics.length()/(double)Math.max(titleHandler.htmlTitle.length(),text.length());
		} else
			altTitleSimiScore = -1;
			
	}
	

	@Override
	public void end(Node node, TitleHandler titleHandler, int nodeId, int depth,
			HashMap<Integer, Features> records) {
		// TODO Auto-generated method stub
		if(!titleHandler.inBody)
			return;
		
		if(node.nodeName() == "li")
			this.liCounter--;
		if(node.nodeName() == "table")
			this.tableCounter--;
		if(this.depth == depth)
			this.isTerminal = true;
		else this.isTerminal = false;
		
		Features f = new Features();
		records.put(nodeId, f);
		
		f.addFeature("sequentialId", nodeId);
		f.addFeature("termial", isTerminal);
		f.addFeature("img", isImage);
		f.addFeature("imgprop", imageItemprop);
		f.addFeature("inList", inList);
		f.addFeature("inTable", inTable);
		f.addFeature("Scaling", hasScaling);
		f.addFeature("simiScore", altTitleSimiScore);
		f.addFeature("distanceToTitle", (nodeId-titleHandler.titleLastSeen)/(double)nodeId);
		
	}

}
