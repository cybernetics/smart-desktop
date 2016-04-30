package com.fs.commons.util;

import java.math.BigDecimal;

public class FinancialUtility {
	/**
	 * @param totalCredit
	 * @param creditAmount
	 * @return
	 */
	public static double addAmounts(double num1, double num2) {
		BigDecimal b1 = new BigDecimal(num1);
		BigDecimal b2 = new BigDecimal(num2);
		BigDecimal b3 = b1.add(b2);
		b3 = b3.setScale(3, BigDecimal.ROUND_HALF_UP);
		double result = b3.doubleValue();
		return result;
	}

	/**
	 * 
	 * @param d
	 * @param e
	 * @return
	 */
	public static double subAmounts(double n1, double n2) {
		BigDecimal b1 = new BigDecimal(n1);
		BigDecimal b2 = new BigDecimal(n2);
		BigDecimal b3 = b1.subtract(b2);
		b3 = b3.setScale(3, BigDecimal.ROUND_HALF_UP);
		double result = b3.doubleValue();
		return result;
	}

	/**
	 * 
	 * @param price
	 * @param qty
	 * @return
	 */
	public static double mutiply(double n1, double n2) {
		BigDecimal b1 = new BigDecimal(n1);
		BigDecimal b2 = new BigDecimal(n2);
		BigDecimal b3 = b1.multiply(b2);
		b3 = b3.setScale(3, BigDecimal.ROUND_HALF_UP);
		double result = b3.doubleValue();
		return result;
	}
	

	/**
	 * 
	 * @param price
	 * @param qty
	 * @return
	 */
	public static double divide(double n1, double n2) {
		BigDecimal b1 = new BigDecimal(n1);
		BigDecimal b2 = new BigDecimal(n2+"");
		BigDecimal b3 = b1.divide(b2,3 , BigDecimal.ROUND_HALF_UP);
		b3 = b3.setScale(3, BigDecimal.ROUND_HALF_UP);
		double result = b3.doubleValue();
		return result;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static double fixAmount(double value) {
		BigDecimal b1 = new BigDecimal(value);
		BigDecimal b2 = b1.setScale(3, BigDecimal.ROUND_HALF_UP);
		return b2.doubleValue();
	}
	public static void main(String[] args) {
		System.out.println(divide(10.1, 30));
	}
}
