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
package com.fs.reflection.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.fs.reflection.MethodCallInfo;

public class ReflectionClient {
	//////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws IOException {
		final ReflectionClient client = new ReflectionClient("localhost", 8765);
		final MethodCallInfo info = new MethodCallInfo("test.ToBeReflected", "sayHello", "Jalal");
		client.callMethod(info);
		System.out.println(info.getResult());
	}

	private final String host;

	private final int port;

	// ad support for connection caching
	public ReflectionClient(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	//////////////////////////////////////////////////////////////////////
	public void callMethod(final MethodCallInfo info) throws IOException {
		final Socket socket = new Socket(this.host, this.port);
		try {
			final ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			final ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			final MethodCallInfo serverCopy = (MethodCallInfo) in.readObject();
			info.set(serverCopy);
			in.close();
			out.close();
		} catch (final ClassNotFoundException e) {
			throw new IOException(e);
		} finally {
			socket.close();
		}
	}
}
