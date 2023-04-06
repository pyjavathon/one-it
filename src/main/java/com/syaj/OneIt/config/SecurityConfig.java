package com.syaj.OneIt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.syaj.OneIt.jwt.JwtAccessDeniedHandler;
import com.syaj.OneIt.jwt.JwtAuthenticationEntryPoint;
import com.syaj.OneIt.jwt.JwtSecurityConfig;
import com.syaj.OneIt.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;




@EnableWebSecurity//기본적인 웹 보안을 활성화하겠다
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//@PreAuthorize 를 메소드 단위로 추가하기 위해 사용
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
		this.tokenProvider = tokenProvider;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			
		.csrf().disable()
		
		.exceptionHandling()
		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.accessDeniedHandler(jwtAccessDeniedHandler)
		
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		
		
		
		.and()
		.authorizeRequests()//httpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다
		.antMatchers("/test/hello").permitAll()//이 api에 대한 접근은 인증없이 허용함
		.antMatchers("/user/login").permitAll()
		.antMatchers("/user/signup").permitAll()
		.anyRequest().authenticated()
		
		.and()
		.apply(new JwtSecurityConfig(tokenProvider));
		
			return http.build();
	}
}
