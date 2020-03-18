/**
 * 
 */
package com.hcl.ecom.rolebasedoauth2.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.ecom.rolebasedoauth2.RoleBasedOauth2Application;
import com.hcl.ecom.rolebasedoauth2.config.OAuthHelper;
import com.hcl.ecom.rolebasedoauth2.dto.ApiResponse;
import com.hcl.ecom.rolebasedoauth2.dto.ChangePasswordRequest;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.User;
import com.hcl.ecom.rolebasedoauth2.service.UserService;
import com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl;

/**
 * @author praveen.verma
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoleBasedOauth2Application.class)
@WebAppConfiguration
public class UserControllerTest {

	@Autowired
	private WebApplicationContext webapp;

	@Autowired
	private OAuthHelper authHelper;

	private MockMvc mockMvc;

	UserDto user, updatedUser;
	
	@InjectMocks 
	UserController userController;
	
	@Mock 
	private UserService userService;

	/*
	 * @Autowired UserServiceImpl userServiceImpl;
	 * 
	 * @Autowired UserController userController;
	 * 
	 * @Mock private UserService userService;
	 */

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
		// Dummy User
		
		
		//--------------------------------
		user = new UserDto();
		user.setId(29);
		user.setFirstName("Bharat-1");
		user.setLastName("Sharma");
		user.setUsername("bharat-1");
		user.setEmail("bharat1@hcl.com");
		user.setPassword("abc1234");
		user.setContact(878765786);
		user.setBillingAddress("HCL IT CITY");
		user.setShippingAddress("HCL IT CITY");
		List<String> roleList = new ArrayList<>();
		roleList.add("USER");
		user.setRole(roleList);
	
	}


	@Test
	public void createTest_success() throws Exception {
		String requestUrl = "/users";

		// Mock here
		  Mockito.when(userService.save(user)).thenReturn(user); 
		  ApiResponse apiResponse=userController.create(user);
		  assertEquals(200,apiResponse.getStatus());
		 
		
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN"); // Valid ROLE
		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc
				.perform(post(requestUrl).with(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print());
		resultActions.andExpect(status().isOk());
		
	}

	@Test
	public void createTest_unAuthorized() throws Exception {
		String requestUrl = "/users";

		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc.perform(post(requestUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	public void createTest_AccessDenied() throws Exception {
		String requestUrl = "/users";

		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_USER"); // invalid Role
		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc
				.perform(post(requestUrl).with(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print());
		resultActions.andExpect(status().isForbidden());
	}

	@Test
	public void getUserTest() throws Exception {
		String requestUrl = "/users/29";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_CLIENT");
		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isOk());
	}

	@Test
	public void getUserTest_UnAuthenticated() throws Exception {
		String requestUrl = "/users/29"; 
		ResultActions resultActions = mockMvc.perform(get(requestUrl)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());

	}

	@Test
	public void listUserTest_success() throws Exception {
		String requestUrl = "/users";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN");
		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isOk());

	}
	@Test
	public void listUserTest_UnAuthenticated() throws Exception {
		String requestUrl = "/users";
		ResultActions resultActions = mockMvc.perform(get(requestUrl)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());

	}
	
	@Test
	public void listUserTest_AccessDenied() throws Exception {
		String requestUrl = "/users";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_USER");
		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isForbidden());

	}
	
	@Test
	public void updateUserTest_success() throws Exception {
		String requestUrl = "/users/update/29";
		updatedUser = user;
		updatedUser.setId(29);
		updatedUser.setFirstName("test");
		updatedUser.setLastName("client");
		updatedUser.setUsername("testclient");
		updatedUser.setEmail("test@hcl.com");
		updatedUser.setPassword("abc1234");
		updatedUser.setContact(878765786);
		updatedUser.setBillingAddress("HCL IT CITY");
		updatedUser.setShippingAddress("HCL IT CITY");
		List<String> updateRoleList = new ArrayList<>();
		updateRoleList.add("CLIENT");
		updatedUser.setRole(updateRoleList);
		
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN"); // Valid ROLE
		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc
				.perform(put(requestUrl).with(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print());
		resultActions.andExpect(status().isOk());
	}
	
	@Test
	public void updateUserTest_unAuthorized() throws Exception {
		String requestUrl = "/users/update/29";

		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc.perform(put(requestUrl).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	public void updateUserTest_AccessDenied() throws Exception {
		String requestUrl = "/users/update/29";

		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_USER"); // invalid Role
		String inputJson = convertObjectToJsonString(user);
		ResultActions resultActions = mockMvc
				.perform(put(requestUrl).with(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print());
		resultActions.andExpect(status().isForbidden());
	}
	
	
	@Test
	public void deleteTest_unAuthenticated() throws Exception {
		String requestUrl = "/users/29";
		
		ResultActions resultActions = mockMvc.perform(delete(requestUrl)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());

	}
	@Test
	public void deleteTest_accessDenied() throws Exception {
		String requestUrl = "/users/29";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_USER");
		ResultActions resultActions = mockMvc.perform(delete(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isForbidden());

	}
	
	@Test
	public void deleteTest_success() throws Exception {
		String requestUrl = "/users/2";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN");
		ResultActions resultActions = mockMvc.perform(delete(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isOk());

	}
	
	@Test
	public void changePasswordTest_success() throws Exception {
		String requestUrl = "/users/changePassword";
		String inputJson = convertObjectToJsonString(new ChangePasswordRequest(29,"abc1234","abc12345"));
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_CLIENT");
		ResultActions resultActions = mockMvc.perform(put(requestUrl).with(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print());
		resultActions.andExpect(status().isOk());

	}
	
	@Test
	public void changePasswordTest_unAuthorized() throws Exception {
		String requestUrl = "/users/changePassword";
		String inputJson = convertObjectToJsonString(new ChangePasswordRequest(29,"abc1234","abc12345"));
		ResultActions resultActions = mockMvc.perform(put(requestUrl)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print());
		resultActions.andExpect(status().isUnauthorized());

	}
	
	@Test
	public void changePasswordTest_accessDenied() throws Exception {
		String requestUrl = "/users/changePassword";
		String inputJson = convertObjectToJsonString(new ChangePasswordRequest(29,"abc1234","abc12345"));
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN");
		ResultActions resultActions = mockMvc.perform(put(requestUrl).with(accessToken)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print());
		resultActions.andExpect(status().isForbidden());

	}
	
	
	

	/***
	 * Convert Object to JSON String
	 * 
	 * @param obj
	 * @return String
	 */
	public String convertObjectToJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
