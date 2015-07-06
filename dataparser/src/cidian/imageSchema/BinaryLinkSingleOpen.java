package imageSchema;

public class BinaryLinkSingleOpen extends BinaryLinker{

	public BinaryLinkSingleOpen(String name) {
		super(name);
		super.left = this;
	}
	public String toString(){
		return this.name + strLink + name(right);
	}
}
