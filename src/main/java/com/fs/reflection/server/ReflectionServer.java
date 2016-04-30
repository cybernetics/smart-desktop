package com.fs.reflection.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.fs.reflection.MethodCallInfo;
import com.fs.reflection.MethodsCaller;

public class ReflectionServer {
	public static final int DEFAULT_PORT = 8765;
	int port;

	/**
	 * Constructor
	 * 
	 * @param port
	 */
	public ReflectionServer(int port) {
		this.port = port;
	}

	/**
	 * Start the server on the port set n the constructor
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		ServerSocket server = new ServerSocket(port);
		System.out.println("Reflection Server started at port : " + port);
		try{
		while (true) {
			System.out.println("Reflection server waiting client connection...");
			Socket client = server.accept();
			System.out.println("Client connected");
			handleClient(client);
		}
		}finally{
			server.close();
		}
	}

	// //////////////////////////////////////////////////////////////////////
	private void handleClient(final Socket client) throws IOException {
		ClientHandler handler = new ClientHandler(client);
		Thread thread = new Thread(handler);
		thread.start();
	}

	// //////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException {
		ReflectionServer server = new ReflectionServer(DEFAULT_PORT);
		server.start();
	}
}

// //////////////////////////////////////////////////////////////////////
class ClientHandler implements Runnable {

	private Socket client;

	// //////////////////////////////////////////////////////////////////////
	public ClientHandler(Socket client) {
		this.client = client;
	}

	// //////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
			Object object = in.readObject();
			if (object instanceof MethodCallInfo) {
				MethodCallInfo info = (MethodCallInfo) object;
				MethodsCaller caller = new MethodsCaller();
				caller.callMethod(info);
				info.setParamters();//workaround to the jasper paramaters by reference.
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				out.writeObject(info);
			} else {
				System.err.println(object + "not instanceof MethodCallInfo");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			System.out.println("Closing client connection");	
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}
}
