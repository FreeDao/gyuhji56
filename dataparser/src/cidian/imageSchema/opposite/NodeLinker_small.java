package imageSchema.opposite;

import imageSchema.BinaryLinkSingleOpen;

public class NodeLinker_small extends BinaryLinkSingleOpen{
	
	public NodeLinker_small() {
		super("小");
	}
	public static void main(String[]args){
		NodeLinker_small pro = new NodeLinker_small();
		System.out.println(pro);
		System.out.println(pro.leftMatch());
	}
}
