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
package com.fs.reflection.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.fs.reflection.MethodCallInfo;
import com.fs.reflection.MethodsCaller;

// //////////////////////////////////////////////////////////////////////
class ClientHandler implements Runnable {

	private final Socket client;

	// //////////////////////////////////////////////////////////////////////
	public ClientHandler(final Socket client) {
		this.client = client;
	}

	// //////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		try {
			final ObjectInputStream in = new ObjectInputStream(this.client.getInputStream());
			final Object object = in.readObject();
			if (object instanceof MethodCallInfo) {
				final MethodCallInfo info = (MethodCallInfo) object;
				final MethodsCaller caller = new MethodsCaller();
				caller.callMethod(info);
				info.setParamters();// workaround to the jasper paramaters by
									// reference.
				final ObjectOutputStream out = new ObjectOutputStream(this.client.getOutputStream());
				out.writeObject(info);
			} else {
				System.err.println(object + "not instanceof MethodCallInfo");
			}
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		} finally {
			System.out.println("Closing client connection");
			try {
				this.client.close();
			} catch (final IOException e) {
			}
		}
	}
}

public class ReflectionServer {
	public static final int DEFAULT_PORT = 8765;

	// //////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws IOException {
		final ReflectionServer server = new ReflectionServer(DEFAULT_PORT);
		server.start();
	}

	int port;

	/**
	 * Constructor
	 *
	 * @param port
	 */
	public ReflectionServer(final int port) {
		this.port = port;
	}

	// //////////////////////////////////////////////////////////////////////
	private void handleClient(final Socket client) throws IOException {
		final ClientHandler handler = new ClientHandler(client);
		final Thread thread = new Thread(handler);
		thread.start();
	}

	/**
	 * Start the server on the port set n the constructor
	 *
	 * @throws IOException
	 */
	public void start() throws IOException {
		final ServerSocket server = new ServerSocket(this.port);
		System.out.println("Reflection Server started at port : " + this.port);
		try {
			while (true) {
				System.out.println("Reflection server waiting client connection...");
				final Socket client = server.accept();
				System.out.println("Client connected");
				handleClient(client);
			}
		} finally {
			server.close();
		}
	}
}
