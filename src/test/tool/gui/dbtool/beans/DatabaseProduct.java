package test.tool.gui.dbtool.beans;

public class DatabaseProduct {

	private String productName;
	private String productVersion;
	private String url;
	private String username;
	private String driverName;
	private String driverVersion;
	private String defaultTransactionIsolation;
	
	/*
	 * sqlserver/mysql分别返回数据库名称
	 * oracle/DB2返回null
	 */
	private String catalog;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverVersion() {
		return driverVersion;
	}
	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}
	public String getDefaultTransactionIsolation() {
		return defaultTransactionIsolation;
	}
	public void setDefaultTransactionIsolation(String defaultTransactionIsolation) {
		this.defaultTransactionIsolation = defaultTransactionIsolation;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	@Override
	public String toString() {
		return  "数据库产品名称：" + productName + "\n" +
				"数据库产品版本："+ productVersion + "\n" +
				"连接URL：" + url + "\n" +
				"当前登录用户：" + username+ "\n" +
				"数据库驱动名称：" + driverName + "\n" +
				"数据库驱动版本："+ driverVersion + "\n" +
				"默认事务隔离级别："	+ defaultTransactionIsolation + "\n" +
				"数据库目录：" + (catalog==null?"":catalog);
	}
}
