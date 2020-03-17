package com.hcl.ecom.rolebasedoauth2.controller;

import com.hcl.ecom.rolebasedoauth2.dto.ApiResponse;
import com.hcl.ecom.rolebasedoauth2.dto.ChangePasswordRequest;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.service.AuthenticationFacadeService;
import com.hcl.ecom.rolebasedoauth2.service.UserService;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	/***
	 * This method used for get all users/admin/client
	 * Only access from ROLE_ADMIN
	 * @return ApiResponse
	 */

	@Secured({ ROLE_ADMIN })
	@GetMapping
	public ApiResponse listOfUsers() {
		log.info(String.format("received request to list user %s",
				authenticationFacadeService.getAuthentication().getPrincipal()));
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findAll());
	}

	/***
	 * This method used for created users/admin/client
	 * Only access from ROLE_ADMIN
	 * @param user
	 * @return ApiResponse
	 */
	@Secured({ ROLE_ADMIN })
	@PostMapping
	public ApiResponse create(@RequestBody UserDto user) {
		// log.info(String.format("received request to create user %s",
		// authenticationFacadeService.getAuthentication().getPrincipal()));
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.save(user));
	}

	/***
	 * This method contains the user/admin/client details
	 * Only access from ROLE_ADMIN, ROLE_USER, ROLE_CLIENT  
	 * @param id
	 * @return ApiResponse
	 */
	@Secured({ ROLE_ADMIN, ROLE_USER, ROLE_CLIENT })
	@GetMapping(value = "/{id}")
	public ApiResponse getUser(@PathVariable long id) {
		// log.info(String.format("received request to get user %s",
		// authenticationFacadeService.getAuthentication().getPrincipal()));
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findOne(id));
	}

	/***
	 * This method contains the delete user/client/admin
	 * Only access from ROLE_ADMIN
	 * @param id
	 * @throws Exception 
	 */
	@Secured({ ROLE_ADMIN })
	@DeleteMapping(value = "/{id}")
	public ApiResponse delete(@PathVariable(value = "id") long id) throws Exception
		{
			log.info(String.format("received request to delete user %s",
					authenticationFacadeService.getAuthentication().getPrincipal()));
		  return new ApiResponse(HttpStatus.OK, SUCCESS, userService.delete(id));
		}
	
	
	
/**
 * This method contains the update user/client/admin
 * Only access from ROLE_ADMIN,ROLE_USER, ROLE_CLIENT
 * @param user
 * @param id
 * @return ApiResponse
 */
	@Secured({ ROLE_ADMIN, ROLE_USER, ROLE_CLIENT })
	@PutMapping("/update/{id}")
	public ApiResponse updateUser(@RequestBody(required = true) UserDto user, @PathVariable long id) {
		log.info(String.format("received request to update user information %s",
				authenticationFacadeService.getAuthentication().getPrincipal()));
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.updateUser(user, id));
	}

	
	/**
	 * This method contains the Change Password of user/client/admin
	 * Only access from ROLE_USER, ROLE_CLIENT
	 * @param changePasswordRequest
	 * @return ApiResponse
	 */
	@Secured({ ROLE_USER, ROLE_CLIENT })
	@PutMapping("/changePassword")
	public ApiResponse changePassword(@RequestBody(required = true) ChangePasswordRequest changePasswordRequest) {
		log.info(String.format("received request to update user password %s",
				authenticationFacadeService.getAuthentication().getPrincipal()));
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.changePassword(changePasswordRequest.getOldPassword(),
				changePasswordRequest.getNewPassword(), changePasswordRequest.getId()));
	}

	
	@PostMapping("/login")
	public ApiResponse authenticate(@RequestParam String username, @RequestParam String password) {
		
		return new ApiResponse(HttpStatus.OK, SUCCESS, userService.authentication(username, password));

	}
	
		

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@Secured("ROLE_USER")
	@GetMapping("/hello-again")
	public String helloAgain() {
		
		
		return "hello again";
	}

}
