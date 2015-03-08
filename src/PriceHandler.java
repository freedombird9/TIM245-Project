import java.util.HashMap;

import org.jsoup.nodes.Node;


public class PriceHandler {
	int priceCounter = 0;
	boolean inDollarSign=false;
	boolean inList = false;
	int listStartDepth = -1;
	String curDollar = "";
	int currencyStartDepth = -1;
	public void start(Node node, TitleHandler titleHandler, int nodeId, int depth, HashMap<Integer, Features> records){
		if(!titleHandler.inBody)
			return;
		String id = node.attr("id")==null?"":node.attr("id").toLowerCase();
		String name = node.attr("name")==null?"":node.attr("name").toLowerCase();
		String cls = node.attr("class")==null?"":node.attr("class").toLowerCase();
		if(cls!=null && cls.indexOf("price")>=0){
			this.priceCounter++;
		}else if(id!=null && id.indexOf("price")>=0){
			this.priceCounter++;
		}else if(name!=null && name.indexOf("price")>=0){
			this.priceCounter++;
		}
		if(node.nodeName()=="#text"){
			String text = node.toString().toLowerCase().trim();
			if(text.indexOf("price")>=0){
				this.priceCounter++;	
			}
			if( this.priceCounter<=0 || text.length()<1){
				return;
			}
			if(text.indexOf("list")>=0){
				inList = true;
				this.listStartDepth = depth-1;
				return;
			}
			if(!containCurrency(text)){
				if(!inDollarSign)
					return;
			}else{
				inDollarSign = true;
				currencyStartDepth = depth-1; 
				curDollar = "";
			}
			curDollar += text;
		}
	}
	
	public void end(Node node, TitleHandler titleHandler, int nodeId, int depth, HashMap<Integer, Features> records){
		if(!titleHandler.inBody)
			return;
		String id = node.attr("id")==null?"":node.attr("id").toLowerCase();
		String name = node.attr("name")==null?"":node.attr("name").toLowerCase();
		String cls = node.attr("class")==null?"":node.attr("class").toLowerCase();
		boolean priceInAttr = this.priceCounter>0?true:false;
		if(cls!=null && cls.indexOf("price")>=0){
			this.priceCounter--;
		}else if(id!=null && id.indexOf("price")>=0){
			this.priceCounter--;
		}
		if(name!=null && name.indexOf("price")>=0){
			this.priceCounter--;
		}
		if(node.nodeName()=="#text"){
			String text = node.toString().toLowerCase().trim();
			if(text.indexOf("price")>=0){
				this.priceCounter--;	
			}
			Features f = records.get(node.hashCode());
			if(f==null){
				f = new Features();
				records.put(node.hashCode(),f);
			}
			if(inDollarSign && depth==currencyStartDepth){
				f.addFeature("termial", true);
				f.addFeature("text", curDollar);
				f.addFeature("priceInAttr", priceInAttr);
				f.addFeature("currency", true);
				f.addFeature("listPrice", inList);
				f.addFeature("distanceToTitle", (nodeId-titleHandler.titleLastSeen)/(double)nodeId);
				f.addFeature("sequentialId", nodeId);
				inDollarSign=false;
//				System.out.println("!!!!!!!!text!!!!!!");
//				System.out.println(curDollar);
//				System.out.println("nodeId:"+nodeId);
//				System.out.println("tc"+titleHandler.titleCounter);
//				System.out.println("title last seen"+titleHandler.titleLastSeen);
			}else{
				f.addFeature("termial", false);
				f.addFeature("text", text);
				f.addFeature("priceInAttr", priceInAttr);
				f.addFeature("currency", false);
				f.addFeature("listPrice", inList);
				f.addFeature("distanceToTitle", (nodeId-titleHandler.titleLastSeen)/(double)nodeId);
				f.addFeature("sequentialId", nodeId);
			}
		}
		if(inList && depth == this.listStartDepth){
			inList=false;
		}
	}
	
	private boolean containCurrency(String text){
		if(text == null || text.length()==0)
			return false;
		if(text.indexOf("$")>=0 || text.indexOf("eu")>=0 || text.indexOf("us")>=0 || text.indexOf("gbp")>=0)
			return true;
		
		return false;
	}

}