import java.util.HashMap;

import org.jsoup.nodes.Node;


public class PriceHandler {
	int priceCounter = 0;
	boolean inDollarSign=false;
	boolean inList = false;
//	int listStartDepth = -1;
	String curDollar = "";
	int currencyStartDepth = -1;
//	int hashId = 0;
	private String website;
	
	public PriceHandler (String website){
		this.website = website;
	}
	
	
	
	public void start(Node node, TitleHandler titleHandler, int depth, HashMap<Integer, Features> records){
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
//			if(text.indexOf("price")>=0){
//				this.priceCounter++;	
//			}
			if( this.priceCounter<=0 || text.length()<1){
				return;
			}
			if(text.indexOf("list")>=0 && text.indexOf("price")>=0){
				inList = true;
//				this.listStartDepth = depth-1;
				return;
			}
			if(!containCurrency(text)){
				if(!inDollarSign)
					return;
			}else{
				inDollarSign = true;
				if(onlyCurrency(text) && node.parent().childNodeSize()==1){
					currencyStartDepth = depth-2;
				}else{
					currencyStartDepth = depth-1;
				}
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
		}else if(name!=null && name.indexOf("price")>=0){
			this.priceCounter--;
		}
		if(inDollarSign){
			if(depth==currencyStartDepth){
				Features f = new Features();
				records.put(nodeId,f);
				f.addFeature("termial", true);
//				f.addFeature("text", curDollar);
				f.addFeature("priceInAttr", priceInAttr);
				f.addFeature("currency", true);
				f.addFeature("listPrice", inList);
				f.addFeature("distanceToTitle", titleHandler.distanceToTitle(nodeId));
				f.addFeature("sequentialId", nodeId);
				addLabel(node, f);
				inDollarSign=false;
				if(inList)
					inList=false;
			}
			return;
		}else if(node.nodeName()=="#text"){
			String text = node.toString().toLowerCase().trim();
			if(text.length()==0){
				return;
			}
//			if(text.indexOf("price")>=0){
//				this.priceCounter--;	
//			}
			Features f = new Features();
			records.put(nodeId,f);
			f.addFeature("termial", false);
//			f.addFeature("text", text);
			f.addFeature("priceInAttr", priceInAttr);
			f.addFeature("currency", false);
			f.addFeature("listPrice", inList);
			f.addFeature("distanceToTitle", titleHandler.distanceToTitle(nodeId));
			f.addFeature("sequentialId", nodeId);
			addLabel(node, f);
		}
	}
	
	private void addLabel(Node node, Features f){
		if(this.website.indexOf("Amazon")>=0){
			//labeling Amazon
			if(node.attr("id").indexOf("priceblock_ourprice")>=0)
				f.addFeature("price", "true");
			else if(node.attr("class").indexOf("priceLarge")>=0 && node.parent().attr("id").indexOf("actualPriceValue")>=0)
				f.addFeature("price", "TRUE");
			else f.addFeature("price", "FALSE");
		}

		else if(this.website.indexOf("ebay")>=0){
			// labeling ebay
			if(node.attr("class").indexOf("notranslate")>=0 && node.attr("id").indexOf("prcIsum")>=0 && node.attr("itemprop").indexOf("price")>=0)
				f.addFeature("price", "TRUE");
			else f.addFeature("price", "FALSE");
		}

		else if(this.website.indexOf("Alibaba")>=0){
			// labeling Alibaba
			if(node.attr("id").indexOf("J-get-last-price")>=0 && node.attr("class").indexOf("glp dot-app-pd")>=0 && 
					node.attr("data-domdot").indexOf("id:3303")>=0 && 
					node.attr("data-dot").indexOf("d_type=get_price&cid=3303")>=0 && node.attr("rel").indexOf("nofollow")>=0)
				f.addFeature("price", "TRUE");
			else if(node.attr("class").indexOf("price proPrice")>=0 && node.attr("itemprop").indexOf("priceCurrency")>=0 &&
						node.attr("content").indexOf("USD")>=0)
						f.addFeature("price", "TRUE");
/*			else if(node.attr("itemprop").indexOf("lowPrice") >= 0)
				f.addFeature("price", "TRUE");
			else if(node.attr("itemprop").indexOf("highPrice") >= 0)
				f.addFeature("price", "TRUE");*/
			else if(node.nodeName().indexOf("h3")>=0 && node.attr("class").indexOf("price-now")>=0 &&
					node.attr("itemprop").indexOf("priceCurrency") >= 0 && node.attr("content").indexOf("USD")>=0)
				f.addFeature("price", "TRUE");
			else if(node.attr("itemprop").indexOf("price")>=0 && node.parent().nodeName().indexOf("h3")>=0 && 
					node.parent().attr("class").indexOf("price-now")>=0 && node.parent().attr("itemprop").indexOf("priceCurrency") >= 0 &&
					node.parent().attr("content").indexOf("USD")>=0)
				f.addFeature("price", "TRUE");
			else f.addFeature("price", "FALSE");
		}
		

		else if(this.website.indexOf("walmart")>=0){
			// labeling Walmart
			if(node.attr("class").indexOf("js-price-display price price-display")>=0){
				f.addFeature("price", "TRUE");
				System.out.println(node.nodeName());
			}				
			else f.addFeature("price", "FALSE");
		}
	}
	
	
	private boolean containCurrency(String text){
		if(text == null || text.length()==0)
			return false;
		if(text.indexOf("$")>=0 || text.indexOf("eu")>=0 || text.indexOf("us")>=0 || text.indexOf("gbp")>=0)
			return true;
		
		return false;
	}

	private boolean onlyCurrency(String text){
		if(text == null || text.length()==0)
			return false;
		if(text=="$"|| text=="eu" || text=="us" || text=="gbp")
			return true;
		
		return false;
	}
}
