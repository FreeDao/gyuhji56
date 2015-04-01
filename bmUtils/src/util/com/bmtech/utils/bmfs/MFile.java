package com.bmtech.utils.bmfs;

import java.io.IOException;

public final class MFile {
	public static final int MAX_NAME_LEN = 64;
	public final int fsId;
	public final String name;
	public final long createTime;
	private long offset = -1;
	private long length = 0;

	// final MDir dir;

	MFile(String name, MDir dir) throws IOException {
		if (name.length() > MAX_NAME_LEN) {
			throw new IOException("file name too long! for'" + name + "'");
		}
		this.name = name;
		this.fsId = dir.locateId(name);
		// this.dir = dir;
		this.createTime = System.currentTimeMillis();
	}

	MFile(String name, final int fsId, byte flag, long createTime)
			throws IOException {
		this.name = name;
		this.fsId = fsId;
		// this.dir = dir;
		this.createTime = createTime;
	}

	// public void delete() throws Exception {
	// this.dir.delete(this);
	// }

	public long getLength() {
		return length;
	}

	public long getCreateTime() {
		return createTime;
	}

	short veryfyPend() {
		return (short) ((fsId >> 16) ^ (fsId & 0x0000ffff));
	}

	public byte flagByte() {
		byte flag = 0;
		return flag;
	}

	public String toString() {
		return this.name;
	}

	// public boolean isMount() {
	// return dir.isMount(this);
	// }

	public static final int marginLen = 16;

	public byte[] marginBytes() {
		byte[] ret = new byte[] { (byte) 'x',// 0
				(byte) 'x',// 1
				(byte) 'x',// 2
				(byte) 'x',// 3
				(byte) 'x',// 4
				(byte) 'x',// 5
				(byte) 'x',// 6
				(byte) 'x',// 7
				(byte) 'x',// 8
				(byte) 'x',// 9
				(byte) 'x',// 10
				(byte) 'x',// 11
				(byte) 'x',// 12
				(byte) 'x',// 13
				(byte) (fsId & 0xff),// 14
				(byte) (fsId >> 4),// 15
		};
		assert ret.length == marginLen;
		return ret;
	}

	void setLength(int len) {
		this.length = len;
	}

	public long getOffset() {
		return offset;
	}

	void setOffset(long offset) {
		this.offset = offset;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean equals(Object o) {
		if (o instanceof MFile) {
			return this.fsId == ((MFile) o).fsId;
		}
		return false;
	}

}