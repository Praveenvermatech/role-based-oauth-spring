package com.hcl.ecom.rolebasedoauth2.service;

import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.User;

import java.util.List;

public interface UserService {

    UserDto save(UserDto user);
    List<UserDto> findAll();
    UserDto findOne(long id);
    void delete(long id);
    UserDto updateUser(UserDto user, long id);
    boolean changePassword(String oldPassword, String newPassword, long id);
    boolean authentication(String username, String password);
    
    
    
}
