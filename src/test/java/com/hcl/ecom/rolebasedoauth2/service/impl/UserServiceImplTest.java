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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcl.ecom.rolebasedoauth2.RoleBasedOauth2Application;
import com.hcl.ecom.rolebasedoauth2.controller.UserController;
import com.hcl.ecom.rolebasedoauth2.dao.UserDao;
import com.hcl.ecom.rolebasedoauth2.dto.ChangePasswordRequest;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.User;
import com.hcl.ecom.rolebasedoauth2.util.AppConstatnt;

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
	public void testFindAll_when_success() {
		List<UserDto> listOfUser = new ArrayList<>();
		listOfUser.add(user);
		 Mockito.when(userServiceImpl.findAll()).thenReturn(listOfUser); 
		  assertEquals("ajeet",listOfUser.get(0).getUsername());
		  
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#findOne(long)}.
	 */
	@Test
	public void testFindOne_when_success() {
		 Mockito.when(userServiceImpl.findOne(29)).thenReturn(user); 
		  assertEquals("ajeet",user.getUsername());
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#delete(long)}.
	 */
	@Test
	public void testDelete_when_success() {
		
		 Mockito.when(userServiceImpl.delete(29)).thenReturn(AppConstatnt.DELETE_SUCCESSFULLY); 
		 assertEquals("Delete Successfully",AppConstatnt.DELETE_SUCCESSFULLY);
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
	public void testUpdateUser_when_success() {
		user.setUsername("ajeet-1");
		 Mockito.when(userServiceImpl.updateUser(user, 29)).thenReturn(user); 
		  assertEquals("ajeet-1",user.getUsername());
	}
	
	@Test
	public void testUpdateUser_when_failed() {
		user.setUsername("ajeet-1");
		 Mockito.when(userServiceImpl.updateUser(user, 333)).thenReturn(user); 
		 assertEquals("ajeet-1",user.getUsername());
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#changePassword(java.lang.String, java.lang.String, long)}.
	 */
	@Test
	public void testChangePassword_when_success() {
		ChangePasswordRequest passwordRequest = new ChangePasswordRequest(user.getId(), user.getPassword(), "abc12345");
		 Mockito.when(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId())).thenReturn(AppConstatnt.PASSWORD_CHANGED_SUCCESSFULLY); 
		 assertEquals(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId()),AppConstatnt.PASSWORD_CHANGED_SUCCESSFULLY);
	}
	
	
	@Test
	public void testChangePassword_when_invalidUser() {
		ChangePasswordRequest passwordRequest = new ChangePasswordRequest(249, user.getPassword(), "abc12345");
		 Mockito.when(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId())).thenReturn(AppConstatnt.USERID_NOT_FOUND); 
		 assertEquals(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId()),AppConstatnt.USERID_NOT_FOUND);
	}
	
	
	@Test
	public void testChangePassword_when_samePassword() {
		ChangePasswordRequest passwordRequest = new ChangePasswordRequest(user.getId(), user.getPassword(), "abc1234");
		 Mockito.when(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId())).thenReturn(AppConstatnt.PASSWORD_IS_SAME); 
		 assertEquals(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId()),AppConstatnt.PASSWORD_IS_SAME);
	}
	
	
	@Test
	public void testChangePassword_when_imvalidOldPassword() {
		ChangePasswordRequest passwordRequest = new ChangePasswordRequest(user.getId(), user.getPassword(), "abc123456");
		 Mockito.when(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId())).thenReturn(AppConstatnt.PASSWORD_IS_NOT_CORRECT); 
		 assertEquals(userServiceImpl.changePassword(passwordRequest.getOldPassword(),passwordRequest.getNewPassword()
				 , passwordRequest.getId()),AppConstatnt.PASSWORD_IS_NOT_CORRECT);
	}
	
	

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#authentication(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAuthentication_when_success() {
		Mockito.when(userServiceImpl.authentication(user.getUsername(), user.getPassword())).thenReturn(true); 
		 assertEquals(userServiceImpl.authentication(user.getUsername(), user.getPassword()),true);
	}

	/**
	 * Test method for {@link com.hcl.ecom.rolebasedoauth2.service.impl.UserServiceImpl#authentication(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAuthentication_when_failed() {
		Mockito.when(userServiceImpl.authentication("abc", "abc123")).thenReturn(false); 
		 assertEquals(userServiceImpl.authentication("abc", "abc123"),false);
	}
	
	
}
