/**
 * 
 */
package com.hcl.ecom.rolebasedoauth2.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

	UserDto user;

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
		user = new UserDto();
		user.setId(2);
		user.setFirstName("test");
		user.setLastName("client");
		user.setUsername("pawan15");
		user.setEmail("pawan15@hcl.com");
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

//		Mockito.when(userService.save(user)).thenReturn(user);
//		ApiResponse apiResponse=userController.create(user);
//		assertEquals(200,apiResponse.getStatus();

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
		String requestUrl = "/users/2";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_CLIENT");
		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());
		resultActions.andExpect(status().isOk());
	}

	@Test
	public void getUserTest_UnAuthenticated() throws Exception {
		String requestUrl = "/users/2"; 
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
