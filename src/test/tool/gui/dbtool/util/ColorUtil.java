package test.tool.gui.dbtool.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import test.tool.gui.common.MyColor;

public class ColorUtil {

	/*
	 * 常用眼睛保护色
	 */
	public static Color XING_REN_HUANG = new Color(250, 249, 222);//杏仁黄
	public static Color QIU_YE_HE = new Color(255,242,222);//秋叶褐
	public static Color YAN_ZHI_HONG = new Color(253,230,224) ;//胭脂红
	public static Color QING_CAO_LU = new Color(227,237,205);//青草绿
	public static Color HAI_TIAN_LAN = new Color(220,226,241);//海天蓝
	public static Color GE_JIN_ZI = new Color(233,235,254);//葛巾紫
	public static Color JI_GUANG_HUI = new Color(234,234,239);//极光灰
	public static Color LU_DOU_SHA = new Color(204,232,207);//绿豆沙	  
	public static Color White = Color.WHITE;//纯白色	 
	
	private static Map<String,MyColor> colorsMap ; 
	
	public static Map<String, MyColor> getColorsMap(){
		if(colorsMap==null){
			colorsMap = new HashMap<String, MyColor>(); 
			MyColor MYCOLOR_XING_REN_HUANG = new MyColor("XING_REN_HUANG","杏仁黄",XING_REN_HUANG);
			MyColor MYCOLOR_QIU_YE_HE = new MyColor("QIU_YE_HE","秋叶褐",QIU_YE_HE);
			MyColor MYCOLOR_YAN_ZHI_HONG = new MyColor("YAN_ZHI_HONG","胭脂红",YAN_ZHI_HONG);
			MyColor MYCOLOR_QING_CAO_LU = new MyColor("QING_CAO_LU","青草绿",QING_CAO_LU);
			MyColor MYCOLOR_HAI_TIAN_LAN = new MyColor("HAI_TIAN_LAN","海天蓝",HAI_TIAN_LAN);
			MyColor MYCOLOR_GE_JIN_ZI = new MyColor("GE_JIN_ZI","葛巾紫",GE_JIN_ZI);
			MyColor MYCOLOR_JI_GUANG_HUI = new MyColor("JI_GUANG_HUI","极光灰",JI_GUANG_HUI);
			MyColor MYCOLOR_LU_DOU_SHA = new MyColor("LU_DOU_SHA","绿豆沙",LU_DOU_SHA);
			MyColor MYCOLOR_White = new MyColor("White","纯白色",White);
			colorsMap.put(MYCOLOR_XING_REN_HUANG.getColorChineseName(), MYCOLOR_XING_REN_HUANG);
			colorsMap.put(MYCOLOR_QIU_YE_HE.getColorChineseName(), MYCOLOR_QIU_YE_HE);
			colorsMap.put(MYCOLOR_YAN_ZHI_HONG.getColorChineseName(), MYCOLOR_YAN_ZHI_HONG);
			colorsMap.put(MYCOLOR_QING_CAO_LU.getColorChineseName(), MYCOLOR_QING_CAO_LU);
			colorsMap.put(MYCOLOR_HAI_TIAN_LAN.getColorChineseName(), MYCOLOR_HAI_TIAN_LAN);
			colorsMap.put(MYCOLOR_GE_JIN_ZI.getColorChineseName(), MYCOLOR_GE_JIN_ZI);
			colorsMap.put(MYCOLOR_JI_GUANG_HUI.getColorChineseName(), MYCOLOR_JI_GUANG_HUI);
			colorsMap.put(MYCOLOR_LU_DOU_SHA.getColorChineseName(), MYCOLOR_LU_DOU_SHA);
			colorsMap.put(MYCOLOR_White.getColorChineseName(), MYCOLOR_White);
		}	
		return colorsMap;
	}
	
	/**
	 * 根据colorChineseName查询，返回对应的MyColor
	 * @param colorChineseName
	 * @return MyColor
	 */
	public static MyColor getColor(String colorChineseName){	
		return getColorsMap().get(colorChineseName);
	}
	/**
	 * 返回所有颜色的中文名称
	 * @return
	 */
	public static List<String> getColorNameList() {
		List<String> list = new ArrayList<String>();
		Map<String, MyColor> myColor = ColorUtil.getColorsMap();
		Set<String> set = myColor.keySet();
		list.addAll(set);
		return list;
	}
}
