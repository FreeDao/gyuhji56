package com.bmtech.lexcion.ip202_114_40_175.port8080.concat.toLine2;

import com.bmtech.utils.io.diskMerge.MRecord;

public class ConcatLine extends MRecord{
	private int[]arr;
	public int index;
	@Override
	protected void init(String str) throws Exception {
		String strs[] = str.split("\\+");
		setArr(new int[strs.length]);
		for(int x = 0; x < strs.length; x++){
			getArr()[x] = Integer.parseInt(strs[x]);
		}
	}

	public String toString(){
		return String.format("%s %s %s %s", arr[0], arr[1], arr[2], arr[3]);
	}
	@Override
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for(int x : getArr()){
			if(sb.length() > 0){
				sb.append("+");
			}
			sb.append(x);
		}
		return sb.toString();
	}

	public int[] getArr() {
		return arr;
	}

	public void setArr(int[] arr) {
		this.arr = arr;
	}

}