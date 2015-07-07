package com.bmtech.nale.addon.sem.dao;

public class RuleDAOException {
	/**
	 * 
	 */
	public static class AlreadyDefinedName extends Exception {
		private static final long serialVersionUID = 1L;

		public AlreadyDefinedName(String excDescription) {
			super(excDescription);
		}
	}

	public static class RuleNodeDefineException extends Exception {
		private static final long serialVersionUID = 1L;

		public RuleNodeDefineException(String excDescription) {
			super(excDescription);
		}
	}
}