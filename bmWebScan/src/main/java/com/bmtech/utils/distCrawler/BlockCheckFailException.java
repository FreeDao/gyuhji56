package com.bmtech.utils.distCrawler;

public class BlockCheckFailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BlockCheckFailException(byte[] checkBlock, byte[] socketChecker) {
		super("check block check fail! '" + new String(checkBlock)
				+ "' not match the client define '" + new String(socketChecker)
				+ "'");
	}

}
