package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import com.fs.commons.locale.Lables;

public class JKToggleButton extends JToggleButton {
	private Color backgroundOnSelection ;//= Color.blue;
	private Border borderOnSelection = BorderFactory.createLoweredBevelBorder();
	private Color colorOnSelection ;//= Color.white;

	private Color normalBackground;// = Color.LIGHT_GRAY;
	private Color normalColor;
	private Border normalBorder = BorderFactory.createEmptyBorder();

	public JKToggleButton() {
		this("");
	}

	public JKToggleButton(String string) {
		super(Lables.get(string));
		init();
	}

	protected void init() {
		setOpaque(true);
		ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					if (normalBackground == null) {
						normalBackground = getBackground();
					}
					if (normalColor == null) {
						normalColor = getForeground();
					}
					if (normalBorder == null) {
						normalBorder = getBorder();
					}
					if(backgroundOnSelection==null){
						backgroundOnSelection=normalColor;
					}
					if(colorOnSelection==null){
						colorOnSelection=normalBackground;
					}
					//setBorder(getBorderOnSelection());
					setBackground(getBackgroundOnSelection());
					//setForeground(getColorOnSelection());

				} else {
					//setBorder(getNormalBorder());
					setBackground(getNormalBackground());
					//setForeground(getNormalColor());
				}
			}
		};
		addItemListener(itemListener);
		setBorder(getNormalBorder());
		setBackground(getNormalBackground());
	}

	public Color getBackgroundOnSelection() {
		return backgroundOnSelection;
	}

	public void setBackgroundOnSelection(Color backgroundOnSelection) {
		this.backgroundOnSelection = backgroundOnSelection;
	}

	public Color getNormalBackground() {
		return normalBackground;
	}

	public void setNormalBackground(Color normalBackground) {
		this.normalBackground = normalBackground;
	}

	public Color getColorOnSelection() {
		return colorOnSelection;
	}

	public void setColorOnSelection(Color colorOnSelection) {
		this.colorOnSelection = colorOnSelection;
	}

	public Color getNormalColor() {
		return normalColor;
	}

	public void setNormalColor(Color normalColor) {
		this.normalColor = normalColor;
	}

	public Border getBorderOnSelection() {
		return borderOnSelection;
	}

	public void setBorderOnSelection(Border borderOnSelection) {
		this.borderOnSelection = borderOnSelection;
	}

	public Border getNormalBorder() {
		return normalBorder;
	}

	public void setNormalBorder(Border normalBorder) {
		this.normalBorder = normalBorder;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		if (isSelected()) {
//			int w = getWidth();
//			int h = getHeight();
//			g.setColor(getBackground()); // selected color
//			g.fillRect(0, 0, w, h);
//			g.setColor(getForeground()); // selected foreground color
//			g.drawString(getText(), (w - g.getFontMetrics().stringWidth(getText())) / 2 + 1, (h + g.getFontMetrics().getAscent()) / 2 - 1);
//		}
	}

}
