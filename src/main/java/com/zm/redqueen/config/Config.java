package com.zm.redqueen.config;

import org.springframework.context.ApplicationContext;

public class Config {
	public static ApplicationContext context;

	private String Ragasiyam;
	private String SIAAuthServerURL;
	private String ZMNotificationServerURL;
	private String ZMVerificationCodeURL;
	private String DbUserName;
	private String DbPassword;
	private String UserCollectionHostName;
	private String UserCollectionPort;
	private String UserCollectionDBName;

	public String getDbUserName() {
		return DbUserName;
	}

	public void setDbUserName(String dbUserName) {
		DbUserName = dbUserName;
	}

	public String getDbPassword() {
		return DbPassword;
	}

	public void setDbPassword(String dbPassword) {
		DbPassword = dbPassword;
	}

	public String getUserCollectionHostName() {
		return UserCollectionHostName;
	}

	public void setUserCollectionHostName(String userCollectionHostName) {
		UserCollectionHostName = userCollectionHostName;
	}

	public String getUserCollectionPort() {
		return UserCollectionPort;
	}

	public void setUserCollectionPort(String userCollectionPort) {
		UserCollectionPort = userCollectionPort;
	}

	public String getUserCollectionDBName() {
		return UserCollectionDBName;
	}

	public void setUserCollectionDBName(String userCollectionDBName) {
		UserCollectionDBName = userCollectionDBName;
	}

	public String getRagasiyam() {
		return Ragasiyam;
	}

	public void setRagasiyam(String ragasiyam) {
		Ragasiyam = ragasiyam;
	}

	public String getSIAAuthServerURL() {
		return SIAAuthServerURL;
	}

	public void setSIAAuthServerURL(String sIAAuthServerURL) {
		SIAAuthServerURL = sIAAuthServerURL;
	}

	public static Config getConfig() {
		return (Config) context.getBean("Config");
	}

	public String getZMNotificationServerURL() {
		return ZMNotificationServerURL;
	}

	public void setZMNotificationServerURL(String zMNotificationServerURL) {
		ZMNotificationServerURL = zMNotificationServerURL;
	}

	public String getZMVerificationCodeURL() {
		return ZMVerificationCodeURL;
	}

	public void setZMVerificationCodeURL(String zMVerificationCodeURL) {
		ZMVerificationCodeURL = zMVerificationCodeURL;
	}
}
