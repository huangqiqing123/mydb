package test.tool.gui.dbtool.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import test.tool.gui.dbtool.consts.Const;

public class ConnUtil {

	private static Logger log = Logger.getLogger(ConnUtil.class);
	private static ConnUtil connUtil;
	public static ConnUtil getInstance(){
		if(connUtil == null){
			connUtil = new ConnUtil();
		}
		return connUtil;
	}
	//私有构造函数
	private ConnUtil(){}

	private Connection conn;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public void closeConnection(){
		try {
			if(conn != null && !conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}			
	}	
}
