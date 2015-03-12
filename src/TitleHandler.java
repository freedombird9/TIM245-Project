import java.util.HashMap;

import org.jsoup.nodes.Node;


public class TitleHandler extends FieldHandler {
	String htmlTitle="";
	int titleCounter = 0;
	int titleLastSeen = -1;
	boolean inBody = false;
	int treeSize=0;
	private String website;
	
	public TitleHandler(int treeSize, String website){
		this.treeSize = treeSize;
		this.website = website;
	}
	public void start(Node node, int nodeId, HashMap <Integer, Features> records, int treeSize){
		String id = node.attr("id")==null?"":node.attr("id").toLowerCase();
		String name = node.attr("name")==null?"":node.attr("name").toLowerCase();
		String cls = node.attr("class")==null?"":node.attr("class").toLowerCase();
		if(node.nodeName()=="body"){
			this.inBody=true;
		}
		
		if(node.nodeName().indexOf("title")>=0){
			if(node.childNodeSize()>0 && node.childNode(0).nodeName()=="#text"){
				//System.out.println("html title:"+node.childNode(0).toString());
				this.htmlTitle = node.childNode(0).toString().toLowerCase().trim();	
			}
			
		}
		if(!inBody)
			return;
		if(cls!=null && cls.indexOf("title")>=0){
			this.titleCounter++;
			titleLastSeen = nodeId;
		}
		if(id!=null && id.indexOf("title")>=0){
			this.titleCounter++;
			titleLastSeen = nodeId;
		}
		if(name!=null && name.indexOf("title")>=0){
			titleLastSeen = nodeId;
			this.titleCounter++;
		}
		if(node.nodeName()=="#text"){
			String text = node.toString().toLowerCase().trim();
			if(text.length()==0 || text.length()>=300)
				return;
			String lcs = lcs(text,this.htmlTitle);
			double score = lcs.length()/(double)Math.max(this.htmlTitle.length(),text.length());
			Features f = records.get(nodeId);
			if(f==null){
				f = new Features();
			}
			records.put(nodeId, f);
			if( this.titleCounter<=0){
				f.addFeature("titleInAttr", 0);
//				f.addFeature("text", text);
				f.addFeature("similarity", score);
				f.addFeature("location", nodeId/(float)treeSize);
			}else{
				f.addFeature("titleInAttr", 1);
//				f.addFeature("text", text);
				f.addFeature("similarity", score);
				f.addFeature("location", nodeId/(float)treeSize);
			}
			Node parent = node.parent();
			if(this.website.indexOf("Amazon")>=0){
				//labeling Amazon
				if(parent.attr("id").contains("productTitle")){
					f.addFeature("class", 1);			
				} else {
					f.addFeature("class", 0);
				}
			} else if(this.website.indexOf("ebay")>=0){
				// labeling ebay
				if(parent.attr("id").contains("vi-lkhdr-itmTitl")){
					f.addFeature("class", 1);					
				} else {
					f.addFeature("class", 0);
				}			
			} else if(this.website.indexOf("Alibaba")>=0){
				// labeling Alibaba
				if(parent.attr("class").contains("title fn")){
					f.addFeature("class", 1);	
				} else {
					f.addFeature("class", 0);
				}
			} else if(this.website.indexOf("walmart")>=0){
				// labeling Walmart
				if(parent.attr("class").contains("product-name"))
					f.addFeature("class", 1);
				else f.addFeature("class", 0);
			}
			
		}		
	}
	public double distanceToTitle(int nodeId){
		if(this.titleLastSeen<0){
			return 1;
		}else{
			double dist =(nodeId-this.titleLastSeen)/(double)treeSize;
			return dist;
		}
	}
	
	public void end(Node node, HashMap <Integer, Features> records){
		String cls = node.attr("class")==null?"":node.attr("class").toLowerCase();
		String id = node.attr("id")==null?"":node.attr("id").toLowerCase();
		String name = node.attr("name")==null?"":node.attr("name").toLowerCase();
		if(node.nodeName().indexOf("title")>=0){
//			info.titleCounter--;
		}
		if(cls!=null && cls.indexOf("title")>=0){
			this.titleCounter--;
		}
		if(id!=null && id.indexOf("title")>=0){
			this.titleCounter--;
		}
		if(name!=null && name.indexOf("title")>=0){
			this.titleCounter--;
		}
	}

}
