package test.tool.gui.dbtool.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {


	/**
	 * 获得异常的堆栈信息，以字符串的形式返回。
	 * @param e
	 * @return
	 */
	public static String getExceptionInfo(Throwable e){	
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw, true);
	    e.printStackTrace(pw);
	    pw.flush();
	    sw.flush();
		return sw.toString();
	}
}
