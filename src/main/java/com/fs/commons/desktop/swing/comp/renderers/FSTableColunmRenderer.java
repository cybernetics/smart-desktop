package com.fs.commons.desktop.swing.comp.renderers;
//package com.fs.swing.components.renderes;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Insets;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.io.Serializable;
//import java.sql.Date;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.Icon;
//import javax.swing.JLabel;
//import javax.swing.JTable;
//import javax.swing.RowSorter;
//import javax.swing.RowSorter.SortKey;
//import javax.swing.SortOrder;
//import javax.swing.SwingUtilities;
//import javax.swing.border.Border;
//import javax.swing.plaf.UIResource;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.JTableHeader;
//
//import com.fs.swing.binding.BindingComponent;
//import com.fs.util.FormatUtil;
//
//import sun.swing.DefaultLookup;
//
//public class FSTableColunmRenderer extends DefaultTableCellRenderer implements UIResource {
//	public FSTableColunmRenderer() {
//		setOpaque(true);
//		setHorizontalAlignment(CENTER);
//		setBackground(Color.LIGHT_GRAY);
//		setBorder(BorderFactory.createLineBorder(Color.black));
//		setPreferredSize(new Dimension(1, 30));
//		setFont(getFont().deriveFont(Font.BOLD));
//	}
//	
//	@Override
//	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//			}
//		return comp;
//	}
//	
//	
//}