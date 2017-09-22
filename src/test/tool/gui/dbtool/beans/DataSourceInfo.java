package test.tool.gui.dbtool.beans;

import java.io.Serializable;

/*
 * bean 记录连接数据库的信息
 * 必须实现Serializable序列化接口，否则无法将DataSourceInfo的实例化对象保存至磁盘。
 */
public class DataSourceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dataSourceName;//数据源名称，有意义的名称。
	private String sn;//唯一标识，当前连接的标识是current，其余是uuid生成。	
	private String dbtype;
	private String username;
	private String pwd;
	private String driverClass;
	private String url;
	
	//-------------------------------------------------------------------
	
	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
//-----------------------------------------------------	
	
	@Override
	public String toString() {
		return getDataSourceName();
	}

	@Override
	public boolean equals(Object anObject) {

		if (this == anObject) {
		    return true;
		}
		if (anObject instanceof DataSourceInfo) {
			DataSourceInfo anotherDataSourceInfo = (DataSourceInfo)anObject;
			
			return anotherDataSourceInfo.getDriverClass().equalsIgnoreCase(this.getDriverClass())
			&& anotherDataSourceInfo.getUrl().equalsIgnoreCase(this.getUrl())
			&& anotherDataSourceInfo.getUsername().equalsIgnoreCase(this.getUsername());
		}
		return false;    
	}	
}
