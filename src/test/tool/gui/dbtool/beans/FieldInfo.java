package test.tool.gui.dbtool.beans;

/*
 * 表字段信息java bean
 */
public class FieldInfo {
	
	private String tableName;//所属表名称
	private String fieldName;//字段名称
	private String fieldType;//字段类型
	private String fieldLength;//字段长度
	private boolean canBeNull;//是否可以为空
	private String constraintType;//该字段的约束类型（主键、外键、check、无）
	private String constraintStatus;//约束状态（已启用，已禁用）
	private String parentTableName;//外键字段关联的父表

	private String parentTableFieldName;//外键字段关联的父表的字段
	private String defaultValue;//字段默认值
	
	//以下2个字段不参与页面的展现
	private String constraintName;//该字段的约束名称
	private String r_constraintName;//该字段的引用约束名称
	
	
	public String getConstraintStatus() {
		return constraintStatus;
	}
	public void setConstraintStatus(String constraintStatus) {
		this.constraintStatus = constraintStatus;
	}
	public String getR_constraintName() {
		return r_constraintName;
	}
	public void setR_constraintName(String name) {
		r_constraintName = name;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldLength() {
		return fieldLength;
	}
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
	public boolean getCanBeNull() {
		return canBeNull;
	}
	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}
	public String getConstraintName() {
		return constraintName;
	}
	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
	public String getConstraintType() {
		return constraintType;
	}
	public void setConstraintType(String constraintType) {
		this.constraintType = constraintType;
	}
	public String getParentTableName() {
		return parentTableName;
	}
	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getParentTableFieldName() {
		return parentTableFieldName;
	}
	public void setParentTableFieldName(String parentTableFieldName) {
		this.parentTableFieldName = parentTableFieldName;
	}
	
}
