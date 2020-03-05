package com.hcl.ecom.rolebasedoauth2.controller;

import com.hcl.ecom.rolebasedoauth2.dto.ApiResponse;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.service.AuthenticationFacadeService;
import com.hcl.ecom.rolebasedoauth2.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public static final String SUCCESS = "success";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationFacadeService authenticationFacadeService;

    @Secured({ROLE_ADMIN})
    @GetMapping
    public ApiResponse listUser(){
        log.info(String.format("received request to list user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findAll());
    }

    @Secured({ROLE_ADMIN, ROLE_USER})
    @PostMapping
    public ApiResponse create(@RequestBody UserDto user){
        log.info(String.format("received request to create user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.save(user));
    }

    @Secured({ROLE_ADMIN, ROLE_USER, ROLE_CLIENT})
    @GetMapping(value = "/{id}")
    public ApiResponse getUser(@PathVariable long id){
        log.info(String.format("received request to update user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findOne(id));
    }

    @Secured({ROLE_ADMIN})
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") long id){
        log.info(String.format("received request to delete user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
        userService.delete(id);
    }
    
    @Secured({ROLE_ADMIN, ROLE_USER, ROLE_CLIENT})
    @PutMapping("/users/{id}")
	public ApiResponse updateUser(@RequestBody(required = true) UserDto user, @PathVariable long id) {
    	 log.info(String.format("received request to update user information %s", authenticationFacadeService.getAuthentication().getPrincipal()));
         return new ApiResponse(HttpStatus.OK, SUCCESS, userService.updateUser(user, id));
	}
	
    @Secured({ROLE_USER, ROLE_CLIENT})
	@PutMapping("/users/changePassword/{id}")
	public ApiResponse changePassword(@RequestBody(required = true) String oldPassword,@RequestBody(required = true) String newPassword, @PathVariable long id) {
    	 log.info(String.format("received request to update user password %s", authenticationFacadeService.getAuthentication().getPrincipal()));
    	 return new ApiResponse(HttpStatus.OK, SUCCESS, ""+userService.changePassword(oldPassword, newPassword, id));
	}
    @Secured({ROLE_USER, ROLE_CLIENT})
	@PutMapping("/users/login")
    public ApiResponse authenticate(@RequestBody(required = true) String username, @RequestBody(required = true) String password) {
    	 log.info(String.format("received request to login user %s", authenticationFacadeService.getAuthentication().getPrincipal()));
    	 return new ApiResponse(HttpStatus.OK, SUCCESS, ""+userService.authentication(username, password));
	  	
    }
    


}
