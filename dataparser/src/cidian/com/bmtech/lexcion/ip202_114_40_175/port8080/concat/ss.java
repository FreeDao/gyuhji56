package com.bmtech.lexcion.ip202_114_40_175.port8080.concat;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.RecordReader;

public class ss {


	void concat() throws Exception{
		File f = new File("/ulks/bkts.simGroup.concat.sort.txt");
		LineReader lr = new LineReader(f);
		while(lr.hasNext()){
			lr.next();
			if(lr.currentLineNumber() % 100000 ==0){
				System.out.println(lr.currentLineNumber());
			}
		}
		System.out.println("--->" + lr.currentLineNumber());
	}


	public static void main(String[]args) throws Exception{
		ss h = new ss();
		h.concat();
	}
}
