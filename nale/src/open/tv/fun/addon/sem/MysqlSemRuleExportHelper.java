package tv.fun.addon.sem;

import java.util.List;

import tv.fun.addon.SemToken;
import tv.fun.addon.sem.dao.VarRuleDao;
import tv.fun.addon.sem.vo.VarRule;

import com.bmtech.utils.rds.RDS;

public class MysqlSemRuleExportHelper extends SemRuleExportHelper {

	MysqlSemRuleExportHelper(String toExportVar, SemToken attachTo, String lines)
			throws Exception {
		super(toExportVar, attachTo, lines);
	}

	@Override
	protected String checkVarName(String toExportVar2) {
		return toExportVar2;
	}

	@Override
	public void save() throws Exception {
		VarRuleDao dao = new VarRuleDao();
		dao.saveRuleDao(this.getToExportVar(), this.getAttachTo(),
				this.getSourceCode());
	}

	public static void main(String[] args) throws Exception {
		RDS rds = RDS.getRDSByDefine("nale", "select * from var_rule ");
		List<VarRule> rules = rds.load(VarRule.class);

		for (VarRule rule : rules) {
			System.out.println(rule);
		}

		MysqlSemRuleExportHelper helper = new MysqlSemRuleExportHelper(
				"vVarss_name_sdslinesssss", SemToken.toSemToken("a.b.c.d"),
				"//#aaaa\n#vVarss_name_sdslinesssss=kkk0");
		System.out.println(helper.getToExportObj());
		helper.save();
	}

}
