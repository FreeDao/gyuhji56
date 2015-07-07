package tv.fun.addon.sem.dao;

import java.util.List;

import tv.fun.addon.SemToken;
import tv.fun.addon.sem.dao.RuleDAOException.AlreadyDefinedName;
import tv.fun.addon.sem.dao.RuleDAOException.RuleNodeDefineException;
import tv.fun.addon.sem.vo.VarRule;

import com.bmtech.utils.rds.RDS;

public class VarRuleDao {
	public VarRule loadVarRuleById(int id) throws Exception,
			RuleNodeDefineException {

		final RDS rds = RDS.getRDSByDefine("nale",
				"select * from var_rule where var_id=?");
		try {
			rds.setInt(1, id);
			List<VarRule> rules = rds.load(VarRule.class);
			if (rules.size() == 0) {
				throw new RuleNodeDefineException(
						"not find var_rule with var_id" + id);
			}
			return rules.get(0);

		} finally {
			rds.close();
		}

	}

	public void saveRuleDao(String exportVar, SemToken attachTo,
			String sourceCode) throws Exception, AlreadyDefinedName {

		final RDS rds = RDS.getRDSByDefine("nale",
				"select * from var_rule where var_name=?");
		try {
			rds.setString(1, exportVar);
			List<VarRule> rules = rds.load(VarRule.class);
			if (rules.size() > 0) {
				throw new AlreadyDefinedName("alread has defined var_name '"
						+ exportVar + "' : " + rules.get(0));
			}
			RDS insertRds = RDS
					.getRDSByDefine(
							"nale",
							"insert into var_rule(var_name, var_script, sem_name, sem_name_suffix)values(?,?,?,?)",
							rds.getMyConnection());
			SemToken sp = attachTo.topPar();

			insertRds.setString(1, exportVar);
			insertRds.setString(2, sourceCode);
			insertRds.setString(3, sp.name);
			insertRds.setString(4, attachTo.suffix());
			insertRds.execute();
		} finally {
			rds.close();
		}

	}
}
