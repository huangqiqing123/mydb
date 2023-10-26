package test.tool.gui.dbtool.util;

import java.util.ArrayList;
import java.util.List;

import test.tool.gui.dbtool.consts.Const;

public class SkinUtil {
	
	/**
	 * 设置皮肤
	 * @param skin
	 * @return
	 */
	public static String setSkin(String skinName) {
		
		if(skinName.equals(ConfigUtil.getConfInfo().get(Const.SKIN).toString())){	
			return "当前系统皮肤与您将要切换的皮肤相同，无需切换!";
		}else{		
			ConfigUtil.getConfInfo().put(Const.SKIN, skinName);
			ConfigUtil.updateConfInfo();
			return "换肤成功，重启后生效！";
		}		
	}
	private static List<String> skinNames;
	
	/**
	 * 取得所有皮肤的名称
	 * @return List<String>
	 */
	public static List<String> getSkinNames(){
		if(skinNames == null){
			skinNames = new ArrayList<String>();
			skinNames.add("Java标准风格");
			skinNames.add("Windows风格");
			skinNames.add("Java6风格");
			skinNames.add("苹果风格-浅蓝");
			skinNames.add("苹果风格-浅绿");
			skinNames.add("苹果风格-深绿");
			skinNames.add("苹果风格-粉红");
			skinNames.add("苹果风格-浅褐");
		}
		return skinNames;
	}
}
