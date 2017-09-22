package test.tool.gui.dbtool.beans;

public class SqlResult {

	private String sql;
	private boolean isSuccess;
	private String result;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		
		return "sql="+sql+",isSuccess="+isSuccess+",result="+result;
	}
	
}
