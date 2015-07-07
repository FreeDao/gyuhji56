package com.bmtech.nale.addon;

import java.util.List;

import com.bmtech.utils.io.LineReader;

public class CorpRecordIterator extends RecordIterator<CorpRecord> {
	int offset = 0;

	public CorpRecordIterator(LineReader lr) {
		super(lr);
	}

	@Override
	protected boolean isLineStart(String line) {
		return line.contains(".htm");
	}

	@Override
	protected CorpRecord newRecord(List<String> list) {

		CorpRecord rec = new CorpRecord(list, offset);
		offset = offset + rec.getLineCount();
		return rec;
	}

}
