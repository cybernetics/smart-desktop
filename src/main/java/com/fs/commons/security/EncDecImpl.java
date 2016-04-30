/* Modification History
 * Version		Author		Date		Purpose
 *=============================================================================
 *1.0               Bashar Nadir    Oct 2005          First version
 *
 */

package com.fs.commons.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fs.commons.security.exceptions.DecryptionException;
import com.fs.commons.security.exceptions.EncryptionException;

/**
 * 
 * <p>
 * Title: QAC
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class EncDecImpl implements EncDec {
	private static final String ALOGORITHM = "DESede";

	private Key key;

	private Cipher cipher;

	/**
	 * 
	 * @param key
	 *            Key
	 */
	public EncDecImpl(Key key) {
		this.key = key;
		try {
			cipher = Cipher.getInstance(ALOGORITHM);
		} catch (NoSuchPaddingException ex) {
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * decrypt
	 * 
	 * @param encryptedText
	 *            byte[]
	 * @return byte[]
	 * @throws DecryptionException
	 */
	public byte[] decrypt(byte[] encryptedText) throws DecryptionException {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encryptedText);
		} catch (InvalidKeyException ex) {
			throw new DecryptionException("Invalid Key " + key, ex);
		} catch (BadPaddingException ex1) {
			throw new DecryptionException("BadPaddingException ", ex1);
		} catch (IllegalBlockSizeException ex1) {
			throw new DecryptionException("IllegalBlockSizeException ", ex1);
		} catch (IllegalStateException ex1) {
			throw new DecryptionException("IllegalStateException ", ex1);
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
	public byte[] encrypt(byte[] text) throws EncryptionException {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(text);
		} catch (InvalidKeyException ex) {
			throw new EncryptionException("Invalid Key " + key, ex);
		} catch (BadPaddingException ex1) {
			throw new EncryptionException("BadPaddingException ", ex1);
		} catch (IllegalBlockSizeException ex1) {
			throw new EncryptionException("IllegalBlockSizeException ", ex1);
		} catch (IllegalStateException ex1) {
			throw new EncryptionException("IllegalStateException ", ex1);
		}
	}

	/**
	 * encrypt
	 * 
	 * @param text
	 *            String
	 * @return byte[]
	 * @throws EncryptionException
	 */
	public byte[] encrypt(String text) throws EncryptionException {
		byte[] bytes = text.getBytes();
		return encrypt(bytes);
	}
}
