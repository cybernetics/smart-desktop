package com.fs.commons.application;

public class ClientInfo {
	int clientId;
	String clientName;
	byte[] logo;
	ClientNls nls;

	public ClientNls getNls() {
		return nls;
	}

	public void setNls(ClientNls nls) {
		this.nls = nls;
	}

	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName
	 *            the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the logo
	 */
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	/**
	 * 
	 * @param fieldValue
	 */
	public void setLogo(Object logo) {
		if (logo instanceof byte[]) {
			setLogo((byte[]) logo);
		}
	}

}
