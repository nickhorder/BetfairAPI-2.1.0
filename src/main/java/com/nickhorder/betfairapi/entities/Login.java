package com.nickhorder.betfairapi.entities;

import java.util.List;

public class Login {
	private String loginStatus;
	private String sessionToken;
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
