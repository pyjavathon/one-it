package com.syaj.OneIt.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUtil {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
	
	public static Optional<String> getCurrentUsername(){
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null) {
			logger.debug("Security Context에 인증 정보가 없습니다.");
			return Optional.empty();
		}
		
		String userEmail = null;
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			userEmail = springSecurityUser.getUsername();
			
		}else if(authentication.getPrincipal() instanceof String) {
			userEmail = (String) authentication.getPrincipal();
		}
		return Optional.ofNullable(userEmail);
	}
}
