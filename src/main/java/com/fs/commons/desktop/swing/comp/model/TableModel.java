//package com.fs.commons.desktop.swing.comp.model;
//
//import javax.swing.table.AbstractTableModel;
//
//import com.fs.commons.util.FinancialUtility;
//
//public abstract class  TableModel extends AbstractTableModel {
//	/**
//	 * 
//	 * @param col
//	 * @return
//	 */
//	public int getIntegerColunmSum(int col) {
//		return (int) getColunmSum(col);
//	}
//
//	/**
//	 * 
//	 * @param col
//	 *            int
//	 * @return float
//	 */
//	public double getColunmSum(int col) {
//		double sum = 0;
//		for (int i = 0; i < getRowCount(); i++) {
//			try {
//				double number=getValueAtAsDouble(i,col);
//				sum=FinancialUtility.addAmounts(sum, number);
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			}
//		}
//		return sum;
//	}
//	
//	/**
//	 * 
//	 * @param row
//	 * @param col
//	 * @return
//	 */
//	private double getValueAtAsDouble(int row, int col) {
//		Object valueAt = getValueAt(row, col);
//		double number =0;
//		if( valueAt!=null && !valueAt.toString().equals("")){
//			number= Double.parseDouble(valueAt.toString().trim());					
//		}
//		return number;
//	}
//
//	public float getValueAtAsFloat(int row, int col) {
//		Object valueAt = getValueAt(row, col);
//		float number =0;
//		if( valueAt!=null && !valueAt.toString().equals("")){
//			number= Float.parseFloat(valueAt.toString().trim());					
//		}
//		return number;
//	}
//
//	public boolean isNumericClumn(int col){
//		Class c = getColumnClass(col);
//		if(c.equals(Integer.class)||c.equals(Float.class)||c.equals(Long.class)){
//			return true;
//		}
//		return false;
//	}
//}
