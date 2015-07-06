package imageSchema;

public class NodeLinkMatcher{
	BinaryLinker linker;
	NodeLinkMatcher(Wu left, BinaryLinker linker, Wu right){
		this.linker = linker;
		linker.left = left;
		linker.right = right;
	}
	
}
