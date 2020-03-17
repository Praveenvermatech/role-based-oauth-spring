package com.hcl.ecom.rolebasedoauth2.service.impl;

import com.hcl.ecom.rolebasedoauth2.dao.RoleDao;
import com.hcl.ecom.rolebasedoauth2.dao.UserDao;
import com.hcl.ecom.rolebasedoauth2.dto.UserDto;
import com.hcl.ecom.rolebasedoauth2.model.Role;
import com.hcl.ecom.rolebasedoauth2.model.RoleType;
import com.hcl.ecom.rolebasedoauth2.model.User;
import com.hcl.ecom.rolebasedoauth2.service.UserService;
import com.hcl.ecom.rolebasedoauth2.util.AppConstatnt;

import org.bouncycastle.openssl.PasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;
   
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userDao.findByUsername(userId);
        if(user == null){
            log.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByUserId.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase())).collect(Collectors.toSet());
        return authorities;
    }

    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(user -> users.add(user.toUserDto()));
        return users;
    }

    @Override
    public UserDto findOne(long id) {
        return userDao.findById(id).get().toUserDto();
    }

    @Override
    public String delete(long id) {
    	
    	User user =userDao.findById(id).get();
    	if(user!=null) {
    		userDao.deleteById(id);
    	}else {
    		throw new UsernameNotFoundException(AppConstatnt.USERID_NOT_FOUND);
    	}
        return AppConstatnt.DELETE_SUCCESSFULLY;
    }

    @Override
    public UserDto save(UserDto userDto) {
        User userWithDuplicateUsername = userDao.findByUsername(userDto.getUsername());
        if(userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
           // log.error(String.format("Duplicate username %", userDto.getUsername()));
            throw new RuntimeException("Duplicate username.");
        }
        User userWithDuplicateEmail = userDao.findByEmail(userDto.getEmail());
        if(userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
           // log.error(String.format("Duplicate email %", userDto.getEmail()));
            throw new RuntimeException("Duplicate email.");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setContact(userDto.getContact());
        user.setBillingAddress(userDto.getBillingAddress());
        user.setShippingAddress(userDto.getShippingAddress());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<RoleType> roleTypes = new ArrayList<>();
        userDto.getRole().stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
        user.setRoles(roleDao.find(userDto.getRole()));
        userDao.save(user);
        User userInfo = userDao.findByUsername(userDto.getUsername());
        userDto.setId(userInfo.getId());
        return userDto;
    }

	@Override
	public UserDto updateUser(UserDto user, long id) {
		User updatedUser= userDao.findById(id).get();
		if(updatedUser == null){
            log.error("Invalid id.");
            throw new UsernameNotFoundException("Invalid id.");
        }
		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setLastName(user.getLastName());
		updatedUser.setContact(user.getContact());
		updatedUser.setShippingAddress(user.getShippingAddress());
		updatedUser.setBillingAddress(user.getBillingAddress());
		return userDao.save(updatedUser).toUserDto();
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword, long id)  {
		boolean isPasswordChanged = false;
		User userInfo = userDao.findById(id).get();
		if(userInfo == null){
            log.error("Invalid id.");
            throw new UsernameNotFoundException("Invalid id.");
        }
		if(oldPassword.equals(newPassword)) {
			 log.error("Old Password and New Password are same.");
		}
		userInfo.setPassword(passwordEncoder.encode(newPassword));
		User user = userDao.save(userInfo);
		if (passwordEncoder.matches(newPassword, user.getPassword()))  {
			isPasswordChanged = true;
		} else {
			isPasswordChanged = false;
		}
		return isPasswordChanged;
	}
	@Override
	public boolean authentication(String username, String password) {
		boolean login = false;
		User existedUser = userDao.findByUsername(username);
		if (existedUser != null) {
			log.info("user information :"+existedUser.getEmail()+", Password: "+existedUser.getPassword());
			if (passwordEncoder.matches(password, existedUser.getPassword()))  {
				login = true;
			} else {
				login = false;
			}
			
		} else {
			login = false;
		}
		return login;
	}

		
}
