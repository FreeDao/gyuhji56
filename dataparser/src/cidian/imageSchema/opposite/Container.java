package imageSchema.opposite;

import imageSchema.Wu;

public class Container extends Wu{

	public Container() {
		super("container");
		this.addProperty(new BigSmallProperty());
		this.addProperty(new InOutProperty());
	}
	public static void main(String[] args) {
		Container c = new Container();
		System.out.println(c);
	}

}
