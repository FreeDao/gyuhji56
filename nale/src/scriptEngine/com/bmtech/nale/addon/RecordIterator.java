package com.bmtech.nale.addon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bmtech.utils.io.LineReader;

public abstract class RecordIterator<T> implements Iterator<T> {
	final LineReader lr;
	private T rec;

	public RecordIterator(LineReader lr) {
		this.lr = lr;
		rec = read();
	}

	@Override
	public boolean hasNext() {
		return rec != null;
	}

	@Override
	public T next() {
		T ret = rec;
		rec = read();
		return ret;
	}

	@Override
	public void remove() {
		throw new RuntimeException("not support #remove()");
	}

	public void close() {
		lr.close();
	}

	protected abstract boolean isLineStart(String line);

	protected abstract T newRecord(List<String> string);

	protected T read() {

		List<String> list = new ArrayList<String>();
		while (lr.hasNext()) {
			String line = lr.next();
			if (isLineStart(line) && list.size() > 0) {
				break;
			}
			list.add(line);
		}
		if (list.size() == 0) {
			return null;
		}
		return newRecord(list);

	}
}
