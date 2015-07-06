package imageSchema;

public class PropertyLinker implements NodeLinkable{
	public final String name;
	final PropertyNode pn;
	public PropertyLinker(String name, PropertyNode pn, BinaryLinker linker) {
		this.name = name;
		this.pn = pn;
	}
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public boolean linkClosed() {
		return false;
	}

}
