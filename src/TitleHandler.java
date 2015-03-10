import java.util.HashMap;

import org.jsoup.nodes.Node;


public class TitleHandler extends FieldHandler {
	String htmlTitle="";
	int titleCounter = 0;
	int titleLastSeen = -1;
	boolean inBody = false;
	public void start(Node node, int nodeId, HashMap <Integer, Features> records){
		String id = node.attr("id")==null?"":node.attr("id").toLowerCase();
		String name = node.attr("name")==null?"":node.attr("name").toLowerCase();
		String cls = node.attr("class")==null?"":node.attr("class").toLowerCase();
		if(node.nodeName()=="body"){
			this.inBody=true;
		}
		
		if(node.nodeName().indexOf("title")>=0){
			if(node.childNodeSize()>0 && node.childNode(0).nodeName()=="#text"){
				System.out.println("html title:"+node.childNode(0).toString());
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
			String lcs = lcs(text,this.htmlTitle);
			double score = lcs.length()/(double)Math.max(this.htmlTitle.length(),text.length());
			Features f = records.get(nodeId);
			if(f==null){
				f = new Features();
			}
			records.put(nodeId, f);
			if( this.titleCounter<=0){
				f.addFeature("titleInAttr", false);
				f.addFeature("text", text);
				f.addFeature("similarity", score);
				f.addFeature("siquentialId", nodeId);
				return;
			}else{
				f.addFeature("titleInAttr", true);
				f.addFeature("text", text);
				f.addFeature("similarity", score);
				f.addFeature("siquentialId", nodeId);
			}
			
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
