package util;

import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;

public class MoneyUtil {
	/**
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmountAsString(int amount) {
		return NumbersToWords.convert(amount);
//		if(amount==0){
//			return Lables.get("PRE_0");
//		}
//		String number = "";
//		int firstDigit = amount % 10;
//		int secondDigit = (amount - firstDigit) % 100;
//		int forthDigit = amount / 1000;
//		int thirdDigit = (amount / 100) % 10;
//		////////////////////////////////////////////////////
//		if(forthDigit!=0){
//			number += Lables.get("THO_" + forthDigit);
//		}
//		////////////////////////////////////////////////////
//		if (thirdDigit != 0) {
//			number+=number.equals("")?"":Lables.get("AND");
//			number += Lables.get("HUD_" + thirdDigit);
//		}
//		////////////////////////////////////////////////////
//		if(secondDigit!=0 || firstDigit!=0){
//			number+=number.equals("")?"":Lables.get("AND");
//			int number2=amount%100;			
//			if (number2 >= 0 && number2<20) {
//				return number+= Lables.get("PRE_"+number2);
//			}
//		}
//		//more than or equal 20
//		if (secondDigit == 0 && firstDigit != 0) {
//			number += Lables.get("PRE_" + firstDigit);
//		} else {
//			if (firstDigit != 0) {
//				number += Lables.get("PRE_" + firstDigit);
//				if (firstDigit < amount) {
//					number += " " + Lables.get("AND") + " ";
//				}
//			}
//			if (secondDigit != 0) {
//				number += Lables.get("PRE_" + secondDigit);
//			}
//		}
//		return GeneralUtility.removeExtraSpaces(number);
	}
	
	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws ApplicationException
	 */
	public static void main(String[] args) throws FileNotFoundException, ApplicationException {
		ApplicationManager.getInstance().init();
		System.out.println();
		System.out.println(getAmountAsString(515));		
		System.exit(0);
	}

}
