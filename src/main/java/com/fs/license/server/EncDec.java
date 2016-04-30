package com.fs.license.server;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import com.fs.commons.security.exceptions.DecryptionException;
import com.fs.commons.security.exceptions.EncryptionException;

public class EncDec {
	private static final String ALOGORITHM = "DESEDE";
	private Cipher cipher;
	private Key key;
	
	/**
	 * @throws Exception 
	 * 
	 */
	public EncDec() throws Exception {
		this("123456781234567812345678".getBytes());
	}
	
	/**
	 * 
	 * @param bs
	 * @throws Exception
	 */
	public EncDec(byte[] keyBytes) throws Exception {
		this.key=new SecretKeySpec(keyBytes, ALOGORITHM);
		try {
			cipher = Cipher.getInstance(ALOGORITHM);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * encrypt
	 * 
	 * @param text
	 *            byte[]
	 * @return byte[]
	 * @throws EncryptionException
	 */
	public byte[] encrypt(byte[] text) throws Exception {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text);
		} catch (InvalidKeyException ex) {
			throw new Exception("Invalid Key " + key, ex);
		} catch (BadPaddingException ex1) {
			throw new Exception("BadPaddingException ", ex1);
		} catch (IllegalBlockSizeException ex1) {
			throw new Exception("IllegalBlockSizeException ", ex1);
		} catch (IllegalStateException ex1) {
			throw new Exception("IllegalStateException ", ex1);
		}
	}


	/**
	 * 
	 * @param encryptedText
	 * @return
	 * @throws DecryptionException
	 */
	public byte[] decrypt(byte[] encryptedText) throws Exception{
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encryptedText);
		} catch (InvalidKeyException ex) {
			throw new Exception("Invalid Key " + key, ex);
		} catch (BadPaddingException ex1) {
			throw new Exception("BadPaddingException ", ex1);
		} catch (IllegalBlockSizeException ex1) {
			throw new Exception("IllegalBlockSizeException ", ex1);
		} catch (IllegalStateException ex1) {
			throw new Exception("IllegalStateException ", ex1);
		}
	}
	
	public static void main(String[] args) throws Exception {
		EncDec enc = new EncDec("FINAL-SOLUTIONS-SOFTWARE".getBytes()); 
		byte[] encData=enc.encrypt("jalal".getBytes());
		byte[] decData=enc.decrypt(encData);
		System.out.println(new String(encData));
		System.out.println(new String(decData));
	}
}
