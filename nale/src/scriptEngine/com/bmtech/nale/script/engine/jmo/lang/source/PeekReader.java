package com.bmtech.nale.script.engine.jmo.lang.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Wraps a Reader to provide peeking into the character stream.
 *
 */
public class PeekReader {
	private final CombinReader cmbReader;
	private final BufferedReader in;
	private final CharBuffer peakBuffer;

	public PeekReader(CombinReader cmbReader, int peekLimit) throws IOException {
		in = new BufferedReader(cmbReader);
		this.cmbReader = cmbReader;
		peakBuffer = CharBuffer.allocate(peekLimit);
		fillPeekBuffer();
	}

	public void close() throws IOException {
		in.close();
	}

	private void fillPeekBuffer() throws IOException {
		peakBuffer.clear();
		in.mark(peakBuffer.capacity());
		in.read(peakBuffer);
		in.reset();
		peakBuffer.flip();
	}

	public int read() throws IOException {
		int c = in.read();
		fillPeekBuffer();
		return c;
	}

	/**
	 * Return a character that is further in the stream.
	 *
	 * @param lookAhead
	 *            How far to look into the stream.
	 * @return Character that is lookAhead characters into the stream.
	 */
	public int peek(int lookAhead) {
		if (lookAhead < 1 || lookAhead > peakBuffer.capacity()) {
			throw new IndexOutOfBoundsException(
					"lookAhead must be between 1 and " + peakBuffer.capacity());
		}
		if (lookAhead > peakBuffer.limit()) {
			return -1;
		}
		return peakBuffer.get(lookAhead - 1);
	}

	public SourcePosition getRelativeSourcePosition(int lineNo, int pos) {
		return cmbReader.getRelativeSourcePosition(lineNo, pos);
	}
}
