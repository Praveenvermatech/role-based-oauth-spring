package com.hcl.ecom.rolebasedoauth2.service;

import java.util.List;

import com.hcl.ecom.rolebasedoauth2.dto.UserDto;

public interface UserService {

    UserDto save(UserDto user);
    List<UserDto> findAll();
    UserDto findOne(long id);
    String delete(long id);
    UserDto updateUser(UserDto user, long id);
    String changePassword(String oldPassword, String newPassword, long id);
    boolean authentication(String username, String password);
    
    
    
}
