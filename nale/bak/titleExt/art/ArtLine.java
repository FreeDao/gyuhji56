package frameParser.titleExt.art;

import frameParser.titleExt.SerialNumber;

public class ArtLine {
	public final String line;
	public final SerialNumber tNum;
	public final ArtLine left;
	public ArtLine(String line, SerialNumber tNum, ArtLine left){
		this.line = line;
		this.tNum = tNum;
		this.left = left;
	}
	public String toString(){
		return tNum + "\t" + line;
	}
}
