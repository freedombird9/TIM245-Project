import java.util.HashMap;
import java.util.Set;


public class Features extends HashMap<String, Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String,Object> features = new HashMap<String, Object>();
	
	public Object getFeature(String key){
		return features.get(key);
	}
	
	public Set<String> getKeys(){
		return features.keySet();
	}
	
	public Object addFeature(String key, Object value){
		return features.put(key, value);
	}
}
