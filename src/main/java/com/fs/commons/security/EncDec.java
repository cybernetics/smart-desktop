/* Modification History
 * Version		Author		Date		Purpose
 *=============================================================================
 *1.0               Bashar Nadir    Oct 2005          First version
 *
 */

package com.fs.commons.security;

import com.fs.commons.security.exceptions.DecryptionException;
import com.fs.commons.security.exceptions.EncryptionException;

public interface EncDec {
	/**
	 * 
	 * @param text
	 *            String
	 * @throws EncryptionException
	 * @return byte[]
	 */
	byte[] encrypt(String text) throws EncryptionException;

	/**
	 * 
	 * @param arr
	 *            byte[]
	 * @throws EncryptionException
	 * @return byte[]
	 */
	byte[] encrypt(byte[] arr) throws EncryptionException;

	/**
	 * 
	 * @param encryptedBytes
	 *            byte[]
	 * @throws DecryptionException
	 * @return byte[]
	 */
	byte[] decrypt(byte[] encryptedBytes) throws DecryptionException;
}
