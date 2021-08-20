package com.nagarro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nagarro.entities.Employees;
import com.nagarro.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user from database
		
		Employees employees = userRepository.getUserByUserName(username);
		if(employees==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		
		CustomUserDetails customUserDetails=new CustomUserDetails(employees);
		
		
		return customUserDetails;
	}

}
