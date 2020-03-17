package com.hcl.ecom.rolebasedoauth2.dto;

public class ChangePasswordRequest {

	private long id;
	private String oldPassword;
	private String newPassword;
	
	
	
	
	public ChangePasswordRequest(long id, String oldPassword, String newPassword) {
		super();
		this.id = id;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	public long getId() {
		return id;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	
	
	
	
}
