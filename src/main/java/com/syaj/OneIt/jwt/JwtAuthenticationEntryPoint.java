package com.syaj.OneIt.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
//유효한 자격증명을 제공하지 않고 접근 시 401 unauthorized 에러를 리턴할 클래스
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
