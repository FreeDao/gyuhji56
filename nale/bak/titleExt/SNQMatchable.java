package frameParser.titleExt;

import java.util.ArrayList;
import java.util.List;

import textMatcher.Pattern.def.CombinResult;
import textMatcher.Pattern.def.MatchNode;
import textMatcher.Pattern.def.QMatchable;
import textMatcher.Pattern.def.QResult;
import textMatcher.Pattern.def.reg.SString;

public class SNQMatchable implements QMatchable{
	@Override
	public String defineInfo() {
		return "snMatcher";
	}

	@Override
	public QResult match(String line) {
		
		ArrayList<SerialNumber>lst = new ArrayList<SerialNumber>();
		
		SerialNumber tn;
		tn = ALBNum_level_2.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = ALBNum_level_1.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = CNNum_a1.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a2.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a3.match(line);
		if(tn != null){
			lst.add(tn);
		}
		tn = CNNum_a4.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		tn = ALBNum.match(line);
		if(tn != null)
			lst.add(tn);
		
		tn = CNNum.match(line);
		if(tn != null){
			lst.add(tn);
		}
		
		List<QResult>qlst = new ArrayList<QResult>();
		for(SerialNumber sn : lst){
			
			MatchNode head = new MatchNode(0, 0, null, null, -1);
			//link prefix
			MatchNode mNode, par;
			par = head;
			mNode = new MatchNode(
					par.end(), 
					sn.prefix.length(), 
					new SString(sn.prefix), 
					par.m == null? null : par,
							par.depth + 1);
			par.addChild(mNode);
			
			//link number
			par = mNode;
			mNode = new MatchNode(
					par.end(), 
					sn.number.length(), 
					new SString(sn.number), 
					par.m == null? null : par,
							par.depth + 1);
			par.addChild(mNode);
			
			//link suffix
			par = mNode;
			mNode = new MatchNode(
					par.end(), 
					sn.suffix.length(), 
					new SString(sn.suffix), 
					par.m == null? null : par,
							par.depth + 1);
			par.addChild(mNode);
			
			qlst.add(head.toMResult());
		}
		
		CombinResult cr = new CombinResult(qlst);
		return cr;
	}
}
