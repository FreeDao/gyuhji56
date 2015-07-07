package com.bmtech.nale.script.engine.jmo.lang.source;

/**
 * The position in the source code.
 *
 */
public class SourcePosition {
	private final int lineNo;
	private final int columnNo;
	private final String sourceName;

	public SourcePosition(String sourceName, int lineNumber, int columnNumber) {
		this.lineNo = lineNumber;
		this.columnNo = columnNumber;
		this.sourceName = sourceName;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof SourcePosition))
			return false;
		SourcePosition pos = (SourcePosition) object;
		return this.lineNo == pos.lineNo && this.columnNo == pos.columnNo;
	}

	@Override
	public String toString() {
		return sourceName + " line " + lineNo + " column " + columnNo;
	}

	public int lineNo() {
		return this.lineNo;
	}

	public String getSourceName() {
		return sourceName;
	}

}
