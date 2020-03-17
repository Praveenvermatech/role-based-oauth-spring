/**
 * 
 */
package com.hcl.ecom.rolebasedoauth2.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcl.ecom.rolebasedoauth2.RoleBasedOauth2Application;
import com.hcl.ecom.rolebasedoauth2.controller.UserController;
import com.hcl.ecom.rolebasedoauth2.dao.UserDao;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.User;

/**
 * @author praveen.verma
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RoleBasedOauth2Application.class)
@WebAppConfiguration
public class UserServiceImplTest {
	
	@MockBean
	UserController userController;
	
	@MockBean
	private UserServiceImpl userServiceImpl;
	
	@MockBean
	UserDao userDao;
	
	UserDto user;
	
	User user1 = new User();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		user = new UserDto();
		user.setId(29);
		user.setFirstName("Ajeet");
		user.setLastName("Sharma");
		user.setUsername("ajeet");
		user.setEmail("ajeet@hcl.com");
		user.setPassword("abc1234");
		user.setContact(878765786);
		user.setBillingAddress("HCL IT CITY");
		user.setShippingAddress("HCL IT CITY");
		List<String> roleList = new ArrayList<>();
		roleList.add("USER");
		user.setRole(roleList);
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		
		  
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#findOne(long)}.
	 */
	@Test
	public void testFindOne() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#delete(long)}.
	 */
	@Test
	public void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#save(com.hcl.ecom.rolebasedoauth2.dto.UserDto)}.
	 */
	@Test
	public void testSave_when_duplicateUsername() {
		user1.setUsername("ajeet");
		Mockito.when(userDao.findByUsername(user.getUsername())).thenReturn(user1); 
		assertEquals("ajeet",user1.getUsername());
		
	}
	
	@Test
	public void testSave_when_duplicateEmail() {
		
		user1.setEmail("ajeet@hcl.com");
		Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(user1); 
		assertEquals("ajeet@hcl.com",user1.getEmail());
		
	}
	
	@Test
	public void testSave_when_success() {
		
		 Mockito.when(userServiceImpl.save(user)).thenReturn(user); 
		  assertEquals("ajeet",user.getUsername());
	}
	

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#updateUser(com.hcl.ecom.rolebasedoauth2.dto.UserDto, long)}.
	 */
	@Test
	public void testUpdateUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#changePassword(java.lang.String, java.lang.String, long)}.
	 */
	@Test
	public void testChangePassword() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#authentication(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAuthentication() {
		fail("Not yet implemented"); // TODO
	}

}
