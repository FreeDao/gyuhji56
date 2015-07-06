package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.Comparator;

import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;

public class WordHashGen2WordHashLinkSort {

	

	public static void main(String[]args) throws Exception{
		WordHashGen2WordHashLinkSort h = new WordHashGen2WordHashLinkSort();
		MRTool.sortFile(new File("/ulks//bkts.simGroup.concat.sort.txt"), new File("/ulks//bkts.simGroup.concat.txt"), ConcatLineRecord.class, new Comparator<MRecord>(){

			@Override
			public int compare(MRecord o1, MRecord o2) {
				int x =  ((ConcatLineRecord)o1).ids[0] - ((ConcatLineRecord)o2).ids[0];
				if(x == 0){
					x =  ((ConcatLineRecord)o1).ids[2] - ((ConcatLineRecord)o2).ids[2];
				}
				return x;
			}
			
		},
		1024 * 1024);
	}


}
