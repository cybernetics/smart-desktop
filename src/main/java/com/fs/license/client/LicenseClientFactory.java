package com.fs.license.client;

public class LicenseClientFactory {
	private static LicenseClient client;
	static{
		try {
			//client=new DefaultLicenseClient();
			client=new HttpLicenseClient();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the client
	 */
	public static LicenseClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public static void setClient(LicenseClient client) {
		LicenseClientFactory.client = client;
	}	
}
