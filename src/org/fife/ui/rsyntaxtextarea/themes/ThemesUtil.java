package org.fife.ui.rsyntaxtextarea.themes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

public class ThemesUtil {

	public static final String ECLIPSE = "eclipse";
	public static final String IDEA = "idea";
	public static final String DARK = "dark";
	public static final String DEFAULT = "default";
	public static final String DEFAULT_ALT = "defaul-alt";
	public static final String DRUID = "druid";

	public static final Map<String,Theme> themes = new HashMap<String, Theme>(); 
	public static void updateTheme(RSyntaxTextArea rSyntaxTextArea,String themeName) {
		try {
			Theme theme = themes.get(themeName);
			if(theme == null){
				theme = Theme.load(ThemesUtil.class.getResourceAsStream(themeName + ".xml"));
				themes.put(themeName, theme);
			}
			theme.apply(rSyntaxTextArea);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
