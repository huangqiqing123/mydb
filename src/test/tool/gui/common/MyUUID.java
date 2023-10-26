package test.tool.gui.common;

import java.util.UUID;

public class MyUUID{

	//获取32位的唯一标识符
	public static String getRandomUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-","");
	}
}
