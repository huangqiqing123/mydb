package test.tool.gui.common;

import java.awt.Color;
import java.io.Serializable;

public class MyColor implements Serializable{

	private static final long serialVersionUID = 1L;
	private String colorEnglishName;
	private String colorChineseName;
	private Color color;

	public MyColor(String colorEnglishName,String colorChineseName,Color color){
		this.colorEnglishName = colorEnglishName;
		this.colorChineseName = colorChineseName;
		this.color = color;
	}

	public String getColorEnglishName() {
		return colorEnglishName;
	}

	public void setColorEnglishName(String colorEnglishName) {
		this.colorEnglishName = colorEnglishName;
	}

	public String getColorChineseName() {
		return colorChineseName;
	}

	public void setColorChineseName(String colorChineseName) {
		this.colorChineseName = colorChineseName;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {		
		return this.getColorChineseName();
	}
}
