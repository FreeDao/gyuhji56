package textmatchers.pattern.def;

public interface QMatchable {

	/**
	 * match a str from index 0 of the str and return the QResult
	 * 
	 * @param str
	 * @return
	 */
	public QResult match(MString str);

}
