package textmatchers.pattern.def.reg;

public class RSZ extends RFW {

	public RSZ(int min, int max) {
		super(min, max, '0', '9');
	}

	public RSZ(int count) {
		this(count, count);
	}

	public RSZ() {
		this(1, 1);
	}

}
