package com.privacity.server.util;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;

@Service
public class UtilService {

	private UserRepository userRepository;
	
	public UtilService(@Autowired UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public Usuario getUser() {
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		return u;
	}    
 
	public String invitationCodeGenerator() {
		return RandomStringUtils.randomAlphanumeric(4).toUpperCase(); 
	}
}
