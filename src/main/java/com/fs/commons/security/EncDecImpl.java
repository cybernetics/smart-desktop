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

	private final Key key;

	private Cipher cipher;

	/**
	 *
	 * @param key
	 *            Key
	 */
	public EncDecImpl(final Key key) {
		this.key = key;
		try {
			this.cipher = Cipher.getInstance(ALOGORITHM);
		} catch (final NoSuchPaddingException ex) {
			ex.printStackTrace();
		} catch (final NoSuchAlgorithmException ex) {
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
	@Override
	public byte[] decrypt(final byte[] encryptedText) throws DecryptionException {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key);
			return this.cipher.doFinal(encryptedText);
		} catch (final InvalidKeyException ex) {
			throw new DecryptionException("Invalid Key " + this.key, ex);
		} catch (final BadPaddingException ex1) {
			throw new DecryptionException("BadPaddingException ", ex1);
		} catch (final IllegalBlockSizeException ex1) {
			throw new DecryptionException("IllegalBlockSizeException ", ex1);
		} catch (final IllegalStateException ex1) {
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
	@Override
	public byte[] encrypt(final byte[] text) throws EncryptionException {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
			return this.cipher.doFinal(text);
		} catch (final InvalidKeyException ex) {
			throw new EncryptionException("Invalid Key " + this.key, ex);
		} catch (final BadPaddingException ex1) {
			throw new EncryptionException("BadPaddingException ", ex1);
		} catch (final IllegalBlockSizeException ex1) {
			throw new EncryptionException("IllegalBlockSizeException ", ex1);
		} catch (final IllegalStateException ex1) {
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
	@Override
	public byte[] encrypt(final String text) throws EncryptionException {
		final byte[] bytes = text.getBytes();
		return encrypt(bytes);
	}
}
