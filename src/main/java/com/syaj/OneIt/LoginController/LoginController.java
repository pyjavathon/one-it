package com.syaj.OneIt.LoginController;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syaj.OneIt.LoginService.UserService;
import com.syaj.OneIt.LoginVo.LoginVo;
import com.syaj.OneIt.LoginVo.TokenVo;
import com.syaj.OneIt.LoginVo.UserVo;
import com.syaj.OneIt.jwt.JwtFilter;
import com.syaj.OneIt.jwt.TokenProvider;


@RestController
@RequestMapping("/login")
public class LoginController {

	private final TokenProvider tokenProvider;
	
	private final UserService userService;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	public LoginController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userService = userService;
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<TokenVo> authorize(@Valid @RequestBody LoginVo loginVo){
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUserEmail(), loginVo.getUserPwd());
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.createToken(authentication);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		
		return new ResponseEntity<>(new TokenVo(jwt), httpHeaders,HttpStatus.OK);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserVo userVo){
		return ResponseEntity.ok(userService.signup(userVo));
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getMyUserInfo(){
		return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
	}
	
	@GetMapping("/user/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<?> getUserInfo(@PathVariable String username){
		return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
	}
}
