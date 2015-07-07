package tv.fun.addon.sem.vo;

public class VarRule {
	private int var_id;
	private String var_name;
	private String var_script;
	private String sem_name;
	private String sem_name_suffix;
	private long create_time;
	private long update_time;
	private boolean isdelete;

	@Override
	public String toString() {
		return "VarRule [var_id=" + var_id + ", var_name=" + var_name
				+ ", var_script=" + var_script + ", sem_name=" + sem_name
				+ ", sem_name_suffix=" + sem_name_suffix + ", create_time="
				+ create_time + ", update_time=" + update_time + ", isdelete="
				+ isdelete + "]";
	}

	public int getVar_id() {
		return var_id;
	}

	public void setVar_id(int var_id) {
		this.var_id = var_id;
	}

	public String getVar_name() {
		return var_name;
	}

	public void setVar_name(String var_name) {
		this.var_name = var_name;
	}

	public String getVar_script() {
		return var_script;
	}

	public void setVar_script(String var_script) {
		this.var_script = var_script;
	}

	public String getSem_name() {
		return sem_name;
	}

	public void setSem_name(String sem_name) {
		this.sem_name = sem_name;
	}

	public String getSem_name_suffix() {
		return sem_name_suffix;
	}

	public void setSem_name_suffix(String sem_name_suffix) {
		this.sem_name_suffix = sem_name_suffix;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
}
