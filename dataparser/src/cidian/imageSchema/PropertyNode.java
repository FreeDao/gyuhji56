package imageSchema;

import java.util.ArrayList;
import java.util.List;

public class PropertyNode extends BinaryLinker{
	private List<BinaryLinker> links = new ArrayList<BinaryLinker>();
	
	public PropertyNode(String name, BinaryLinker ...links){
		super(name);
		
		
		if(links.length < 2){
			throw new RuntimeException("properties at least has two links");
		}
		for(BinaryLinker bl : links){
			if(bl.right != null){
				throw new RuntimeException("left node is not null:" + bl);
			}
			bl.left = this;
			this.links.add(bl);
		}
	}

//	private void besureLinkIsOpen(BinaryLinker link){
//		if(link.left != null){
//			throw new RuntimeException("left node is not null:" + link);
//		}
//		if(link.right != null){
//			throw new RuntimeException("left node is not null:" + link);
//		}
//	}
//	private void add(BinaryLinker node){
//		besureLinkIsOpen(node);
//		node.left = this;
//		this.links.add(node);
//	}
	@Override
	/**
	 * property is closed if there is link is closed
	 */
	public boolean linkClosed() {
		for(NodeLinkable n : links){
			if(!n.linkClosed()){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName());
		sb.append("[");
		for(int x = 0; x < this.links.size(); x ++){
			BinaryLinker bl = this.links.get(x);
			sb.append(bl);
			if(x == 0){
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
