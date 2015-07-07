package frameParser.titleExt.art;

import java.util.ArrayList;

import frameParser.titleExt.SerialNumber;

public class ArtBranch {

	ArrayList<ArtSegment>segments = new ArrayList<ArtSegment>();
	ArtSegment par;
	ArtBranch(ArtSegment seg, ArtSegment par){
		this.segments.add(seg);
		seg.myBranch = this;
		this.par = par;
	}
	public ArtSegment getTail(){
		return segments.get(segments.size() -1);
	}
	public boolean patternMatch(ArtSegment segment) {
		ArtSegment seg = segments.get(segments.size() -1);
		SerialNumber num = seg.getTailNum();
		String thisP = num.pattern();
		String itsP = segment.getHeadLine().tNum.pattern();
		if(thisP.equals(itsP)){
			return true;
		}
		return false;
	}
	public boolean canAppend(ArtSegment segment) {
		ArtSegment seg = segments.get(segments.size() -1);
		SerialNumber num = seg.getTailNum();
		
		if(num.next().equals(segment.getHeadLine().tNum)){
			return true;
		}
		return false;
	}
	public void append(ArtSegment segment) {
		this.segments.add(segment);
		segment.myBranch = this;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(ArtSegment seg : segments){
			sb.append(seg);
			sb.append("\n");
		}
		return sb.toString();
	}
}
