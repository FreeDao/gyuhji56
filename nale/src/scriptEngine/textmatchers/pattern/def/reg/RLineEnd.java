package textmatchers.pattern.def.reg;

/**
 * any char except \r \n
 * 
 * @author mudie
 * 
 */
public class RLineEnd extends REnum {
	public RLineEnd() {
		super(new char[] { '\r', '\n' });
	}

	@Override
	public String defineInfo() {
		return "$RN";
	}
}
