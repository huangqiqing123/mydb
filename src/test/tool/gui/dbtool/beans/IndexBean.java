package test.tool.gui.dbtool.beans;

import java.util.List;

public class IndexBean {

	private String tableName;
	private String indexName;
	private boolean isUniqueIndex;
	private boolean isPrimaryIndex;
	
	private List<String> colNameList;//索引关联列  列表
	private List<String> colASC_DESC;//索引关联列  升降序
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public boolean isUniqueIndex() {
		return isUniqueIndex;
	}
	public void setIsUniqueIndex(boolean isUniqueIndex) {
		this.isUniqueIndex = isUniqueIndex;
	}
	public boolean isPrimaryIndex() {
		return isPrimaryIndex;
	}
	public void setIsPrimaryIndex(boolean isPrimaryIndex) {
		this.isPrimaryIndex = isPrimaryIndex;
	}
	public List<String> getColNameList() {
		return colNameList;
	}
	public void setColNameList(List<String> colNameList) {
		this.colNameList = colNameList;
	}
	public List<String> getColASC_DESC() {
		return colASC_DESC;
	}
	public void setColASC_DESC(List<String> colASC_DESC) {
		this.colASC_DESC = colASC_DESC;
	}
	
	@Override
	public String toString() {
		
		return this.getIndexName()+" "+this.getTableName()+" "+this.getColNameList()+" "+this.getColASC_DESC()+" "+this.isUniqueIndex();
	}
	
	
}
