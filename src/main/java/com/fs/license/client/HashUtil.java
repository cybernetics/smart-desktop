package com.fs.license.client;

public class HashUtil {

	/**
	 * 
	 */
	public HashUtil() {
	}

	/**
	 * 
	 * @param src
	 * @return
	 */
	public String hash(String src) {
		String str = "";
		byte[] bytes = src.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			str += "" + bytes[i] + "-";
		}
		return str;
	}

	/**
	 * 
	 * @param src
	 * @return
	 */
	public String deHash(String src) {
		String str = "";
		String[] bytes = src.split("-");
		for (int i = 0; i < bytes.length; i++) {
			String digit = bytes[i];
			if (digit != null && !digit.equals("")) {
				str += "" + ((char) Integer.parseInt(digit));
			}
		}
		return str;
	}

	public static void main(String[] args) {
		HashUtil hash = new HashUtil();
		String h = hash.hash("46-46-46-46-46-46-46-46-46-72-97-99-107-101-100-32-76-105-99-101-110-115-101-46-46-46-46-46-46-46");
		String d = hash.deHash(h);

		System.out.println(h);
		System.out.println(d);
	}

}
