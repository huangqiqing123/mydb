package test.tool.gui.dbtool.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KeyWordUtil {

	private static List<String> keyWordList;//常用sql关键字
	public static List<String> getKeyWordList(){
		if(keyWordList == null || keyWordList.size()==0){
			String dbType = DBUtil.getDBProductInfo().getProductName();
			keyWordList = new ArrayList<String>();
		
			//公共关键字
			keyWordList.add("create");//数据定义
			keyWordList.add("drop");
			keyWordList.add("alter");
			keyWordList.add("select");//数据操纵
			keyWordList.add("select * from");
			keyWordList.add("insert into");
			keyWordList.add("update");
			keyWordList.add("grant");//数据控制
			keyWordList.add("revoke");
			keyWordList.add("between");
			keyWordList.add("union");
			keyWordList.add("exist");
			keyWordList.add("join");
			keyWordList.add("left join");
			keyWordList.add("right join");
			keyWordList.add("full join");
			keyWordList.add("count");//统计函数
			keyWordList.add("sum");
			keyWordList.add("avg");
			keyWordList.add("max");
			keyWordList.add("min");
			keyWordList.add("table");//对象
			keyWordList.add("view");
			keyWordList.add("index");
			keyWordList.add("primary key");//约束
			keyWordList.add("foreign key");
			keyWordList.add("unique");
			keyWordList.add("not null");
			keyWordList.add("constraint");
			keyWordList.add("modify");
			keyWordList.add("column");
			keyWordList.add("clustered");
			keyWordList.add("where");
			keyWordList.add("order by");
			keyWordList.add("group by");
			keyWordList.add("having by");
			keyWordList.add("distinct");
			keyWordList.add("all");
			keyWordList.add("values");
			keyWordList.add("delete from");
			keyWordList.add("default");
			keyWordList.add("varchar");
			keyWordList.add("varchar2");
			keyWordList.add("database");
			keyWordList.add("check");
			keyWordList.add("and");
			keyWordList.add("or");
			
			//sqlserver特有关键字
			if(dbType.contains("MICROSOFT SQL SERVER")){			
				keyWordList.add("nocheck");
			}
			//oracle特有关键字
			if(dbType.contains("ORACLE")){			
				
				keyWordList.add("cascade");
				keyWordList.add("purge");
				keyWordList.add("alter system set recyclebin=on scope=both;");//开启垃圾桶
				keyWordList.add("alter system set recyclebin=off scope=both;");//关闭垃圾桶
				keyWordList.add("purge dba_recyclebin;");//清空垃圾桶
				keyWordList.add("flashback table <表名称> to before drop;");//从垃圾桶中恢复删除的表
				
			}
			//MYSQL特有关键字
			if(dbType.contains("MYSQL")){		
			
				keyWordList.add("alter table <表名称> engine=<存储引擎名称>;");//修改指定表的存储引擎
				
				keyWordList.add("describe <表名称>;");
				
				keyWordList.add("show create table <表名称>;");//生成指定表的建表SQL
				keyWordList.add("show index from <表名称>;");//查询指定表索引信息
				keyWordList.add("show tables;");//显示当前数据库下有哪些表
				keyWordList.add("show databases;");//显示有哪些数据库
				keyWordList.add("show table status;");	//显示所有表的状态信息
				keyWordList.add("show table status like \"<表名称>\";");//显示指定表的状态信息
				keyWordList.add("show full columns from <表名称>;");	//显示指定表的列信息
				keyWordList.add("select version();");//查询当前数据库的版本信息
				keyWordList.add("select user();");//显示当前登录用户
				keyWordList.add("select database();");//显示当前连接的数据库
				keyWordList.add("set foreign_key_checks='off';");//关闭外键约束
				keyWordList.add("set foreign_key_checks='on';");//启用外键约束
				keyWordList.add("show engines;");//查看当前数据库支持的存储引擎
				
				keyWordList.add("use <数据库名称>;");//切换使用的数据库
				
			}
			
			//自定义关键字
			keyWordList.add("getchildren");
			keyWordList.add("desc");
			keyWordList.add("generateInsertSql");
			keyWordList.add("generateCreateSqlForTable");
			keyWordList.add("generateCreateSqlForView");
			keyWordList.add("disableFK");//禁用指定表的外键约束
			keyWordList.add("disableAllFK");//禁用所有数据库表的外键约束
			keyWordList.add("enableFK");
			keyWordList.add("enableAllFK");
			
			//排序
			Collections.sort(keyWordList, new Comparator<String>(){

				/*
				 * int compare(Object o1, Object o2) 返回一个基本类型的整型，
				 * 返回负数表示：o1 小于o2，
				 * 返回0 表示：o1和o2相等，
				 * 返回正数表示：o1大于o2。
				 */
				public int compare(String o1, String o2) {
				
					//按照升序排列
					return o1.compareTo(o2);
				}		
			});  
		}
		return keyWordList;
	}
	/*
	 * 刷新关键字列表，在切换数据库时使用。
	 * 关键字列表会根据当前数据库类型，进行动态组装。
	 */
	public static void refreshKeyWordList(){
		if(keyWordList != null){	
			keyWordList.clear();
		}
	}
	/*
	 * 获取以指定入参开头的关键字
	 */
	private static List<String> keyWordListBeginWith = new ArrayList<String>();
	public static List<String> getKeyWordbeginWith(String pre){
		String lowerPre = pre.toLowerCase();
		keyWordListBeginWith.clear();
		int size = getKeyWordList().size();
		for(int i=0;i<size;i++){
			if(keyWordList.get(i).startsWith(lowerPre)){
				keyWordListBeginWith.add(getKeyWordList().get(i));
			}
		}
		return keyWordListBeginWith;
	}
}
