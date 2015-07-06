package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import com.bmtech.utils.io.diskMerge.MRecord;

public class ConcatLineRecord  extends MRecord{
		public int ids[];
		public ConcatLineRecord(){
			ids = null;
		}
		
		@Override
		public void init(String str) throws Exception {
			String tokens[] = str.trim().split("\\+");
			ids = new int[tokens.length];
			for(int x = 0; x < ids.length; x ++){
				ids[x] = Integer.parseInt(tokens[x]);
			}
		}

		@Override
		public String serialize() {
			StringBuilder sb = new StringBuilder();
			for(int x : ids){
				if(sb.length() > 0){
					sb.append("+");
				}
				sb.append(x);
			}
			return sb.toString();
		}
	}