package crawl.baseInfo;

public class SInfo {
	final String id;
	final String name;
	final String type;
	public SInfo(String line){
		String[] tokens = line.split("\t");
		Integer.parseInt(tokens[0]);
		id =  tokens[0];
		name = tokens[1];
		type = tokens[5];
	}
}
