package com.fs.commons.util;
import java.util.Random;

/////////////////////////////////////////////////////////////////////////////////////////
//Author : Mohamed Kiswani
//Since  : 5-2-2010
///////////////////////////////////////////////////////////////////////////////////////

public final class PasswordGenerator {
	//////////////////////////////////////////////////////////////


	//////////////////////////////////////////////////////////////
	public static String generateNumricPassword(int numberOfChar) {
		Random randomGenerator = new Random();
		String password="";
		for (int i = 1; i <= numberOfChar; ++i) {
			password += randomGenerator.nextInt(10);
		}
		return password;
	}
	////////////////////////////////////////////////////////////
	public static String getAlphapticPassowrds(int numberOfChar,boolean islowerCase) {
		double randomNumber;
		double randomNumberSetup;
		char randomCharacter;
		String password="";
		for (int i = 0; i < numberOfChar; i++) {
			randomNumber = Math.random();
			randomNumberSetup = (randomNumber * 26 + 'a');
			randomCharacter = (char) randomNumberSetup  ;
			password += randomCharacter;
			
		}
		return islowerCase? password :password.toUpperCase();
	}
	////////////////////////////////////////////////////////////	
	public static String generateMixPassword(int n) {
	    char[] pw = new char[n];
	    int c  = 'A';
	    int  r1 = 0;
	    for (int i=0; i < n; i++)
	    {
	      r1 = (int)(Math.random() * 3);
	      switch(r1) {
	        case 0: c = '0' +  (int)(Math.random() * 10); break;
	        case 1: c = 'a' +  (int)(Math.random() * 26); break;
	        case 2: c = 'A' +  (int)(Math.random() * 26); break;
	      }
	      pw[i] = (char)c;
	    }
	    return new String(pw);
	  }
	//////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		System.err.println(PasswordGenerator.generateMixPassword(8));
	}
}