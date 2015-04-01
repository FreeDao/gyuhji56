package test;

import com.bmtech.utils.io.diskMerge.MRecord;

public class TestRecord extends MRecord {
	int value = 0;

	@Override
	protected void init(String str) throws Exception {
		value = Integer.parseInt(str);
	}

	@Override
	public String serialize() {
		return value + "";
	}

}