package test;

import java.io.File;

import com.bmtech.utils.io.diskMerge.MRTool;
import com.bmtech.utils.io.diskMerge.MRecord;
import com.bmtech.utils.io.diskMerge.MReduceSelect;

public class TestReduce {
	static File dir = new File("test/merge");

	public static void main(String[]args) throws Exception{
		MRTool.reduce(
				new File(dir, "r2.txt"), 
				new File(dir, "r1.txt"), TestRecord.class, 
				new MReduceSelect(){

					@Override
					public boolean equals(MRecord me1, MRecord me2) {
						return ((TestRecord)me2).value == ((TestRecord)me1).value;
					}

					@Override
					public MRecord select(MRecord me1, MRecord me2) {
						return me1;
					}

				});


	}


}
