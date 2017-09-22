package test.tool.gui.dbtool.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

import test.tool.gui.common.SysFontAndFace;

@SuppressWarnings("serial")
public class LineNumber extends JComponent {

	//此处字体须保证与jtextArea中的字体一致，否则可能会引起行号与实际行有落差。
	private final  Font DEFAULT_FONT = SysFontAndFace.font15;
	private final  int HEIGHT = Integer.MAX_VALUE - 1000000;
	// Set right/left margin
	private final  int MARGIN = 5;
	// Line height of this LineNumber component
	private int lineHeight;
	// Line height of this LineNumber component
	private int fontLineHeight;
	private int currentRowWidth;
	// Metrics of this LineNumber component
	private FontMetrics fontMetrics;

	/**
	 * Convenience constructor for Text Components
	 */
	public LineNumber() {
			setForeground(Color.blue);
			setFont(DEFAULT_FONT);
			setPreferredSize(10);//设置初始显示行数			
	}

	public void setPreferredSize(int row) {
		int width = fontMetrics.stringWidth(String.valueOf(row));
		if (currentRowWidth < width) {
			currentRowWidth = width;
			setPreferredSize(new Dimension(2 * MARGIN + width, HEIGHT));
		}
	}

	public void setFont(Font font) {
		super.setFont(font);
		fontMetrics = getFontMetrics(getFont());
		fontLineHeight = fontMetrics.getHeight();
	}

	/**
	 * The line height defaults to the line height of the font for this
	 * component. The line height can be overridden by setting it to a positive
	 * non-zero value.
	 */
	public int getLineHeight() {
		if (lineHeight == 0)
			return fontLineHeight;
		else
			return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		if (lineHeight > 0)
			this.lineHeight = lineHeight;
	}

	public int getStartOffset() {
		return 4;
	}

	public void paintComponent(Graphics g) {
		int lineHeight = getLineHeight();
		int startOffset = getStartOffset();
		Rectangle drawHere = g.getClipBounds();
		g.setColor(getBackground());
		g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
		// Determine the number of lines to draw in the foreground.
		g.setColor(getForeground());
		int startLineNumber = (drawHere.y / lineHeight) + 1;
		int endLineNumber = startLineNumber + (drawHere.height / lineHeight);
		int start = (drawHere.y / lineHeight) * lineHeight + lineHeight
				- startOffset;
		for (int i = startLineNumber; i <= endLineNumber; i++) {
			String lineNumber = String.valueOf(i);
			int width = fontMetrics.stringWidth(lineNumber);
			g.drawString(lineNumber, MARGIN + currentRowWidth - width, start);
			start += lineHeight;
		}
		setPreferredSize(endLineNumber);
	}
}