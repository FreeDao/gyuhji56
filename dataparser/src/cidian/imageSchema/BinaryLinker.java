package imageSchema;

public class BinaryLinker implements NodeLinkable{
	public static final String strLink = "-->";
	public static final String strHead = " : ";
	protected final String name;
	NodeLinkable left, right;
	public BinaryLinker(String name) {
		this(name, null, null);
	}
	public BinaryLinker(
			String name, NodeLinkable left, NodeLinkable right) {
		this.name = name;
		this.left = left;
		this.right = right;
	}
	public String getName() {
		return name;
	}
	public boolean leftMatch(){
		return left != null;
	}
	
	public boolean rightMatch(){
		return right != null;
	}
	public void linkLeft(BinaryLinker linker){
		this.left = linker.right;
	}
	public void linkRight(BinaryLinker linker){
		this.right = linker.left;
	}
	protected String name(NodeLinkable nl){
		if(nl == null){
			return "*";
		}else{
			return nl.getName();
		}
	}
	public String toString(){
		return this.name + strHead + name(left) + strLink + name(right);
	}
	@Override
	public boolean linkClosed() {
		return left != null && right != null;
	}

	
}
