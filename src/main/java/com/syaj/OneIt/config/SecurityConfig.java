package com.syaj.OneIt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.syaj.OneIt.jwt.JwtAccessDeniedHandler;
import com.syaj.OneIt.jwt.JwtAuthenticationEntryPoint;
import com.syaj.OneIt.jwt.JwtAuthenticationFilter;
import com.syaj.OneIt.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity // 기본적인 웹 보안을 활성화하겠다
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) 
@RequiredArgsConstructor
public class SecurityConfig {
	
	
	private final TokenProvider tokenProvider;
	private final RedisTemplate redisTemplate;
	
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// ACL(Access Control List, 접근 제어 목록)의 예외 URL 설정
		return (web) -> web
							.ignoring()
							.requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적 리소스들
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable().httpBasic().disable().formLogin().disable()
				//.addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				.and().authorizeRequests()// httpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다
				.antMatchers("/test/hello").permitAll()// 이 api에 대한 접근은 인증없이 허용함
				.antMatchers("/user/login").permitAll()
				.antMatchers("/user/signup").permitAll()
				.antMatchers("/product/**").permitAll()
				.antMatchers("/index.html").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/v3/api-docs").permitAll()
				.antMatchers("/v3/api-docs/**").permitAll()
				.anyRequest()
				.authenticated()

				.and()
				.addFilterBefore(new JwtAuthenticationFilter(tokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
				.headers()
				.frameOptions().sameOrigin();
				

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
