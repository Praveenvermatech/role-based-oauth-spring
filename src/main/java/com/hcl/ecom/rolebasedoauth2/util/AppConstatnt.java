package com.hcl.ecom.rolebasedoauth2.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppConstatnt {

	public static final String CLIENT_ID = "hcl-client";
	public static final String CLIENT_SECRET = "abc123";
	public static final String GRANT_TYPE_PASSWORD = "password";
	public static final String AUTHORIZATION_CODE = "authorization_code";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String IMPLICIT = "implicit";
	public static final String SCOPE_READ = "read";
	public static final String SCOPE_WRITE = "write";
	public static final String TRUST = "trust";
	
	
	
	/***
	 * Convert Object to JSON String
	 * 
	 * @param obj
	 * @return String
	 */
	public static String convertObjectToJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
