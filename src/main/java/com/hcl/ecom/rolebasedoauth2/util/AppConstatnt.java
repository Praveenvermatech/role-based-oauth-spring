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
	public static final String DELETE_SUCCESSFULLY = "Delete Successfully";
	public static final String USERID_NOT_FOUND = "Invalid User id.";
	public static final String PASSWORD_IS_SAME = "Old Password and New Password are same.";
	public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password Changed successfully.";
	public static final String PASSWORD_NOT_CHANGED = "Password does not Changed.";
	public static final String PASSWORD_IS_NOT_CORRECT = "Old Password is not correct. Please give the correct password";
	
	
	
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
