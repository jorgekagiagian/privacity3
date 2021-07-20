package com.privacity.common.dto;

import lombok.Data;

@Data
public class UserDTO{

	private String username;
	
	//@PrivacityPGPOut
	private String usernameToShow;
	

    private String keyCodePrivacity;	

}
