package com.appauth.domain;

public class Token {
	String token;

	public Token() {}
	
	public Token(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
