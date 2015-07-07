package tv.fun.addon;

public class SemToken {
	public final String name;
	private SemToken par;

	private SemToken(String name, SemToken par) {
		this.name = name.trim().toLowerCase();
		if (this.name.length() < 1) {
			throw new RuntimeException("error SemToken, empty name not allowed");
		}
		this.par = par;
	}

	private SemToken(String name) {
		this(name, null);
	}

	public boolean hasPar() {
		return par != null;
	}

	public SemToken getPar() {
		return par;
	}

	public SemToken topPar() {
		if (par == null) {
			return this;
		}
		return par.topPar();
	}

	public String toStringPattern() {
		return "<" + this.toString() + ">";
	}

	@Override
	public String toString() {
		if (par == null) {
			return this.name;
		} else {

			return this.par.toString() + "." + this.name;
		}
	}

	@Override
	public boolean equals(Object other) {
		try {
			SemToken st = (SemToken) other;
			if (par != null) {
				if (par.equals(st.par)) {
					return this.name.equalsIgnoreCase(st.name);
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static SemToken toSemToken(String define) {
		String tokens[] = define.split("\\.");
		SemToken par = null;
		for (String x : tokens) {
			SemToken me = new SemToken(x, par);
			par = me;
		}

		return par;
	}

	public String suffix() {
		String ret = "";
		SemToken st = this;
		while (true) {
			if (st.par == null) {
				break;
			}
			if (ret.length() != 0) {
				ret = "." + ret;
			}
			ret = st.name + ret;
			st = st.par;
		}
		return ret;
	}
}
