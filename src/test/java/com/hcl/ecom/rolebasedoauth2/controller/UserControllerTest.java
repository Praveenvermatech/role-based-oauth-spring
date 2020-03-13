/**
 * 
 */
package com.hcl.ecom.rolebasedoauth2.controller;

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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.User;
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

	@MockBean
	UserServiceImpl userServiceImpl;

	@MockBean
	UserController userController;

	UserDto user;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webapp).apply(springSecurity()).build();
		// Dummy User
		user = new UserDto();
		user.setId(2);
		user.setFirstName("test");
		user.setLastName("client");
		user.setUsername("dummy");
		user.setEmail("dummy@hcl.com");
		user.setPassword("abc1234");
		user.setContact(878765786);
		user.setBillingAddress("HCL IT CITY");
		user.setShippingAddress("HCL IT CITY");
		List<String> roleList = new ArrayList<>();
		roleList.add("USER");
		user.setRole(roleList);

	}

	@Test
	public void testHelloAnonymous() throws Exception {

		ResultActions resultActions = mockMvc.perform(get("/users/hello")).andDo(print());
		resultActions.andExpect(status().isOk()).andExpect(content().string("hello"));

	}

	@Test
	public void testHelloAuthenticated() throws Exception {
		RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_USER");
		ResultActions resultActions = mockMvc.perform(get("/users/hello").with(bearerToken)).andDo(print());

		resultActions.andExpect(status().isOk()).andExpect(content().string("hello"));

	}

	@Test
	public void testHelloAgainAnonymous() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/users/hello-again")).andDo(print());

		resultActions.andExpect(status().isUnauthorized());
	}

	@Test
	public void testHelloAgainAuthenticated() throws Exception {
		RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_USER");
		ResultActions resultActions = mockMvc.perform(get("/users/hello-again").with(bearerToken)).andDo(print());

		resultActions.andExpect(status().isOk()).andExpect(content().string("hello again"));
	}

	@Test
	public void createTest() throws Exception {
		String requestUrl = "/users";
		String inputJson = convertObjectToJsonString(user);
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN");
		// Mock here
		Mockito.when(userServiceImpl.save(user)).thenReturn(user);

		ResultActions resultActions = mockMvc
				.perform(post(requestUrl).with(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print());

		resultActions.andExpect(status().isOk());
	}

	@Test
	public void getUserTest() throws Exception {
		String requestUrl = "/users";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_CLIENT");
		// Mock here
		Mockito.when(userServiceImpl.findOne(2)).thenReturn(user);

		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());

		resultActions.andExpect(status().isOk());

	}

	@Test
	public void getUserTest_UnAuthenticated() throws Exception {
		String requestUrl = "/users";
		// Mock here
		Mockito.when(userServiceImpl.findOne(2)).thenReturn(user);
		
		ResultActions resultActions = mockMvc.perform(get(requestUrl)).andDo(print());

		resultActions.andExpect(status().isUnauthorized());

	}
	
	
	public void listUserTest() throws Exception {
		String requestUrl = "/users";
		RequestPostProcessor accessToken = authHelper.addBearerToken("dummy", "ROLE_ADMIN");
		
		List<UserDto> userList = new ArrayList<>();
		userList.add(user);
		
		// Mock here
		Mockito.when(userServiceImpl.findAll()).thenReturn(userList);

		ResultActions resultActions = mockMvc.perform(get(requestUrl).with(accessToken)).andDo(print());

		resultActions.andExpect(status().isOk());

	}
	
	
	public void listUserTest_UnAuthenticated() throws Exception {
		String requestUrl = "/users";
		
		ResultActions resultActions = mockMvc.perform(get(requestUrl)).andDo(print());

		resultActions.andExpect(status().isUnauthorized());

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
