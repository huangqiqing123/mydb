package test.tool.gui.dbtool.consts;

public class Const {

	//用户文件--存储在应用程序同级目录下
	public static final String DATA_SOURCE_PATH = "config/datasource.data";
	public static final String BACK_SQL_PATH = "config/back_sql.data";
	public static final String CONF_PATH = "config/conf.data";
	public static final String DB_DRIVER_PATH = "config/db_driver.properties";	
	
	//日志文件--存储在应用程序同级目录下的log文件夹下，与log4j.properties中的日志路径保持一致。
	public static final String LOG = "log/db.log";
	
	//数据源连接列表中，用于表示当前连接的 key
	public static final String CURRENT = "current";
	
	//------------CONF_PATH = "config/conf.data";-----------------------
	//sql编辑器字体
	public static final String SQL_FONT = "sql_font";
	//Jtable列表字体
	public static final String JTABLE_FONT = "jtable_font";
	//树节点 字体
	public static final String JTREE_FONT = "jtree_font";
	//记事本 字体
	public static final String NOTEPAD_FONT = "notepad_font";
	//是否启用智能提示
	public static final String IS_ENABLE_SMART_TIPS = "is_enable_smart_tips";
	//事务是否自动提交
	public static final String IS_AUTO_COMMIT = "is_auto_commit";
	//查询结果列表的 行高
	public static final String JTABLE_LINE_HEIGHT = "jtable_line_height";
	//jTREE 操作在sql编辑区是否同步生成sql
	public static final String IS_GENERATE_SQL = "is_generate_sql";
	//点击关闭按钮，执行关闭还是最小化托盘（1：关闭，2：最小化到托盘，3：弹出供用户选择）
	public static final String CLOSE_ACTION = "close_action";
	//眼睛保护色
	public static final String EYE_SAFETY_COLOR = "eye_safety_color";
	//当前系统皮肤（对应枚举类 SkinEnum）
	public static final String SKIN = "skin";
	//是否启用日志功能(true,false)
	public static final String IS_LOG = "is_log";

	//---------------以下功能待实现---------------------
}






