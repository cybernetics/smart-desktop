package com.fs.commons.apps.instance;

import java.io.IOException;
import java.net.ServerSocket;

import com.fs.commons.util.GeneralUtility;

public class InstanceManager {
	
	/**
	 * 
	 */
	public InstanceManager(){
	}
	
	/**
	 * 
	 * @param id
	 * @throws InstanceException
	 */
	public static void registerInstance(int id ) throws InstanceException{
		if(Boolean.valueOf(System.getProperty("fs.commons.singleInstance","false"))){
			try {
				ServerSocket server=new ServerSocket(id);
				GeneralUtility.startFakeThread(server);
				//server.accept();
			} catch (IOException e) {
				throw new InstanceException( e);
			}
		}
	}	
}
