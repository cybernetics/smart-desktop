package com.fs.reflection.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.fs.reflection.MethodCallInfo;

public class ReflectionClient {
	private String host;
	private int port;

	//ad support for connection caching
	public ReflectionClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	//////////////////////////////////////////////////////////////////////	
	public void callMethod(MethodCallInfo info) throws IOException{
		Socket socket = new Socket(host, port);
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(info);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			MethodCallInfo serverCopy = (MethodCallInfo) in.readObject();
			info.set(serverCopy);
			in.close();
			out.close();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} finally {			
			socket.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws IOException{
		ReflectionClient client=new ReflectionClient("localhost", 8765);
		MethodCallInfo info=new MethodCallInfo("test.ToBeReflected", "sayHello", "Jalal");
		client.callMethod(info);
		System.out.println(info.getResult());
	}
}
