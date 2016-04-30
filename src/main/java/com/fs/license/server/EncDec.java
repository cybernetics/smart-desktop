/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	public static void main(final String[] args) throws Exception {
		final EncDec enc = new EncDec("FINAL-SOLUTIONS-SOFTWARE".getBytes());
		final byte[] encData = enc.encrypt("jalal".getBytes());
		final byte[] decData = enc.decrypt(encData);
		System.out.println(new String(encData));
		System.out.println(new String(decData));
	}

	private Cipher cipher;

	private final Key key;

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
	public EncDec(final byte[] keyBytes) throws Exception {
		this.key = new SecretKeySpec(keyBytes, ALOGORITHM);
		try {
			this.cipher = Cipher.getInstance(ALOGORITHM);
		} catch (final Exception e) {
			throw e;
		}

	}

	/**
	 *
	 * @param encryptedText
	 * @return
	 * @throws DecryptionException
	 */
	public byte[] decrypt(final byte[] encryptedText) throws Exception {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key);
			return this.cipher.doFinal(encryptedText);
		} catch (final InvalidKeyException ex) {
			throw new Exception("Invalid Key " + this.key, ex);
		} catch (final BadPaddingException ex1) {
			throw new Exception("BadPaddingException ", ex1);
		} catch (final IllegalBlockSizeException ex1) {
			throw new Exception("IllegalBlockSizeException ", ex1);
		} catch (final IllegalStateException ex1) {
			throw new Exception("IllegalStateException ", ex1);
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
	public byte[] encrypt(final byte[] text) throws Exception {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
			return this.cipher.doFinal(text);
		} catch (final InvalidKeyException ex) {
			throw new Exception("Invalid Key " + this.key, ex);
		} catch (final BadPaddingException ex1) {
			throw new Exception("BadPaddingException ", ex1);
		} catch (final IllegalBlockSizeException ex1) {
			throw new Exception("IllegalBlockSizeException ", ex1);
		} catch (final IllegalStateException ex1) {
			throw new Exception("IllegalStateException ", ex1);
		}
	}
}
