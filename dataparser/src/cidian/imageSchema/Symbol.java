package imageSchema;

public final class Symbol implements NodeLinkable{
	public final String name;
	public Symbol(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public boolean linkClosed() {
		return true;
	}
	

}
