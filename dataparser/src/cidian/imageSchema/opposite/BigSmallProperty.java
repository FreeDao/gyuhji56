package imageSchema.opposite;

import imageSchema.PropertyNode;

public class BigSmallProperty extends PropertyNode{

	public BigSmallProperty() {
		super("big-small", 
				new NodeLinker_big(),
				new NodeLinker_small());
	}

	public static void main(String[]args){
		BigSmallProperty pro = new BigSmallProperty();
		System.out.println(pro);
		System.out.println(pro.leftMatch());
	}
}
