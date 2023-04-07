package com.syaj.OneIt.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.IncorrectClaimException;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	private final TokenProvider tokenProvider;

	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException,ServletException{
		
		String accessToken = resolveToken(request);
		String requestURI = request.getRequestURI();
		
		try {
			if(accessToken != null && tokenProvider.validateToken(accessToken)) {
				Authentication authentication = tokenProvider.getAuthentication(accessToken);//토큰 유효하면 athentication 객체 받아와서 security context에 set
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}",authentication.getName(),requestURI);
			}
		}catch (IncorrectClaimException e) {
			SecurityContextHolder.clearContext();
			logger.debug("Invalid JWT token");
			response.sendError(403);
		}catch(UsernameNotFoundException e) {
			SecurityContextHolder.clearContext();
			logger.debug("Can't find user");
			response.sendError(403);
		}
		
		filterChain.doFilter(request, response);
	}

	// Request Header 에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
