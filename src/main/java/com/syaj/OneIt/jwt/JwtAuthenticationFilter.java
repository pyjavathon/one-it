package com.syaj.OneIt.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	private final TokenProvider tokenProvider;

	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,ServletException{
		
		String jwt = resolveToken((HttpServletRequest) request);
		if (!jwt.isEmpty())
			jwt.replace("Bearer ", "");

		if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT"); // 허용할 request http METHOD : POST, GET,
																					// DELETE, PUT
		res.setHeader("Access-Control-Allow-Headers",
				"Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,observe");
		if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
			res.setStatus(HttpStatus.OK.value());
		} else {
			chain.doFilter(request, response);
		}
	}

	// Request Header 에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
	private String resolveToken(HttpServletRequest request) {
		return request.getHeader(AUTHORIZATION_HEADER);
	}
}
