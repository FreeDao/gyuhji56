package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.IOException;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

public class WordHashGen2WordHashLink {
	int blockSize = 1024 * 32;
	int slotSize = 1024;
	String arr[][] = new String[slotSize][];
	int size = 0;
	synchronized void addTo(String value){
		int[]pos = getPos(size);
		besureMemoryExists(pos);
		arr[pos[0]][pos[1]] = value;
		size ++;
	}
	public String get(int index){
		int[] pos = getPos(index);
		if(arr[pos[0]] == null){
			return null;
		}
		return arr[pos[0]][pos[1]];
	}
	private void besureMemoryExists(int []pos) {
		if(arr[pos[0]] == null){
			arr[pos[0]] = new String[blockSize];
		}		
	}
	private int[] getPos(int x) {
		return new int[]{
				x / blockSize,
				x % blockSize,	
		};
	}
	void loadLines() throws IOException{
		LineReader lr = new LineReader("/ulks/ulk-refine");
		while(lr.hasNext()){
			if(0 == lr.currentLineNumber() % 10000){
				System.out.println("loading " + lr.currentLineNumber());
			}
			String line = lr.next();
			addTo(line);
		}
		lr.close();
	}
	final int minMatch = 5;
	class checker{
		//		int fromLine;
		final String fromTokensx[];
		boolean rightMatch = false, leftMatch = false;
		int rightFrom, leftFrom;
		checker(int fromLine){
			this.fromTokensx = get(fromLine).split(" ");
			rightMatch = false;
			leftMatch = false;
		}

		boolean check(int toIndex){
			rightMatch = false;
			leftMatch = false;
			rightFrom = leftFrom = 0;
			String[]toTokens = get(toIndex).split(" ");
			int ret = checkConcat(fromTokensx, toTokens, minMatch);
			if(ret == -1){
				ret = checkConcat(toTokens, fromTokensx, minMatch);
				if(ret != -1){
					leftMatch = true;
					leftFrom = ret;
					return true;
				}
			}else{
				rightMatch = true;
				rightFrom = ret;
				return true;
			}
			return false;
		}
	}

	static int checkConcat(String fromTokens[], String[]toTokens, int MinMatch){
		//			String[]toTokens = get(toLine).split(" ");
		int ret = -1;
		int checkLen =  fromTokens.length - MinMatch + 1;
		for(int x = 0; x < checkLen; x ++){
			if(fromTokens[x].equals(toTokens[0])){
				MatchResult type = null;
				int toIndex = -1;
				int fromEnd = fromTokens.length - x - 1;
				for(int z = 1; z <= fromEnd; z ++){
					if(z == fromEnd){
						type = MatchResult.RightConcat;
						break;
					}
					toIndex = z;
					if(toIndex >= toTokens.length){
						type = MatchResult.Include;
						break;
					}
					if(fromTokens[x + z].equals(toTokens[toIndex])){
						continue;
					}else{
						type = MatchResult.NOT;
						break;
					}
				}
				if(type == null){
					continue;
				}else if(type == MatchResult.Include){
					//useless? FIXME 
				}else if(type == MatchResult.RightConcat){
					if(x == 0){
						type = MatchResult.beIncluded;
						ret = fromTokens.length - x;
						return ret;
					}else{
						ret = fromTokens.length - x;
						return ret;
					}
				}
			}
		}
		return ret;
	}
	static enum MatchResult{
		NOT, RightConcat, LeftConcat, Include, beIncluded;
	}
	LogHelper log = new LogHelper("ana");
	public void ana(int fromIndex, int[]checkLines) throws IOException{
		checker ck = new checker(fromIndex);
		
		for(int x = 0; x< checkLines.length; x ++){
			int toIndex = checkLines[x];
			if(fromIndex == toIndex){
				continue;
			}
			boolean match = ck.check(toIndex);
			if(!match){
				continue;
			}else{
				if(ck.leftMatch){
					lw.writeLine(toIndex + "+" + fromIndex + "+" + ck.leftFrom);
//					log.info("leftMatch %s, \n\t%s \n\t%s", ck.leftFrom, get(fromIndex), get(toIndex));
				}else if(ck.rightMatch){
//					log.info("rightMatch %s, \n\t%s \n\t%s", ck.rightFrom, get(fromIndex), get(toIndex));
					lw.writeLine(fromIndex + "+" + toIndex + "+" + ck.rightFrom);
				}
//				Consoler.readString("~");
			}
		}
	}
	LineWriter lw;
	public void concat() throws IOException{
		LineReader lr = new LineReader("/ulks//bkts.simGroup");
		lw = new LineWriter("/ulks//bkts.simGroup.concat.txt", false);
		while(lr.hasNext()){
			if(lr.currentLineNumber() % 10000 == 0){
				System.out.println("--------------------" + lr.currentLineNumber());
			}
			String line = lr.next().trim();
			String tokens[] = line.split(" ");
			if(tokens.length == 1){
				log.debug("skip line %s", line);
				continue;
			}
			int fromIndex = Integer.parseInt(tokens[0]);
			int[] to = new int[tokens.length - 1];
			for(int x = 1; x < tokens.length; x ++){
				int num = Integer.parseInt(tokens[x]);
				to[x - 1] = num;
			}
			ana(fromIndex, to);
		}
		lr.close();
		lw.close();
	}
	public static void main(String[]args) throws IOException{
		WordHashGen2WordHashLink h = new WordHashGen2WordHashLink();
		h.loadLines();
		h.concat();
	}
}
