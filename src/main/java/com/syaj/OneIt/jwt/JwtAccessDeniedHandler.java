package com.syaj.OneIt.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
//권한이 존재하지 않는 경우 403 forbidden 에러 리턴
public class JwtAccessDeniedHandler implements AccessDeniedHandler{

	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
