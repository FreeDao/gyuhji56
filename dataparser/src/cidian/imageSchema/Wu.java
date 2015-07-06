package imageSchema;

import java.util.ArrayList;
import java.util.List;

public class Wu implements NodeLinkable{

	private List<NodeLinkable> properties = new ArrayList<NodeLinkable>();
	final String name;

	public Wu(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	
	protected void addProperty(NodeLinkable nl){
		this.properties.add(nl);
	}
	public List<NodeLinkable>getClosedLink(){
		ArrayList<NodeLinkable>ret = new ArrayList<NodeLinkable>();
		for(NodeLinkable nl : this.properties){
			if(nl.linkClosed()){
				ret.add(nl);
			}
		}
		return ret;
	}
	@Override
	public boolean linkClosed() {
		for(NodeLinkable nl: properties){
			if(!nl.linkClosed()){
				return false;
			}
		}
		return true;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(":{");
		
		for(int x = 0; x < this.properties.size(); x ++){
			if(x > 0){
				sb.append(", ");
			}
			sb.append(this.properties.get(x));
			
		}
		sb.append("}");
		return sb.toString();
	}
}
