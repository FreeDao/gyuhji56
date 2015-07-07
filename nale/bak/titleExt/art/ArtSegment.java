package frameParser.titleExt.art;

import java.util.ArrayList;

import frameParser.titleExt.SerialNumber;

public class ArtSegment{
	ArrayList<ArtLine>lines = new ArrayList<ArtLine>();
	ArtBranch myChild;
	ArtBranch myBranch;
	ArtSegment(ArtLine line){
		this.addArtLine(line);
	}
	public void addArtLine(ArtLine line){
		this.lines.add(line);
	}
	public void setBranchChild(ArtBranch b) {
		if(this.myChild != null){
			throw new RuntimeException();
		}
		this.myChild = b;
	}
	public ArtLine getHeadLine() {
		return lines.get(0);
	}
	public SerialNumber endNum() {
		for(int x = lines.size() - 1; x >= 0; x --){
			ArtLine aline = lines.get(x);
			if(aline.tNum != null){
				return aline.tNum;
			}
		}
		return null;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(ArtLine aline : lines){
			sb.append(aline.line);
			sb.append('\n');
		}
		return sb.toString();
	}
	public SerialNumber getTailNum() {
		SerialNumber num = null;
		for(ArtLine al : lines){
			if(al.tNum != null){
				num = al.tNum;
			}
		}
		return num;
	}

}