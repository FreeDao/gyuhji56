package imageSchema.opposite;

import imageSchema.BinaryLinkSingleOpen;
import imageSchema.PropertyNode;

public class InOutProperty extends PropertyNode{
	public InOutProperty() {
		super("in-out", 
				new BinaryLinkSingleOpen("in"),
				new BinaryLinkSingleOpen("out"));
	}

	public static void main(String[]args){
		InOutProperty pro = new InOutProperty();
		System.out.println(pro);
		System.out.println(pro.leftMatch());
	}
}
