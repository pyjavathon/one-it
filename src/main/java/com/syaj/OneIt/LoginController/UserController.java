package com.syaj.OneIt.LoginController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginService.UserService;
import com.syaj.OneIt.LoginVo.UserRequestVo;
import com.syaj.OneIt.LoginVo.UserRequestVo.Logout;
import com.syaj.OneIt.jwt.JwtAuthenticationFilter;
import com.syaj.OneIt.jwt.TokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user controller")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	public UserController(AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userService = userService;
	}

	@Operation(summary = "user register/modify method")
	@PostMapping(value = "/userRegister")
	public void userRegister(@RequestBody UserEntity user) {
		userService.userRegister(user);
	}

	@GetMapping(value = "/allUserSelect")
	public List<UserRequestVo.Login> allUserSelect() {
		return userService.allUserSelect();
	}

	@GetMapping(value = "/userSearch")
	public UserRequestVo.Login userSearch(@RequestParam Long id) {
		return userService.userSearch(id);
	}

	@DeleteMapping(value = "/userDel")
	public void userDel(@RequestParam Long id) {
		userService.userDel(id);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody UserRequestVo.Login userRequestVo){
		
		
		return ResponseEntity.ok(userService.login(userRequestVo));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@Valid @RequestBody UserRequestVo.Logout userRequestVo){
		
		return ResponseEntity.ok(userService.logout(userRequestVo));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserRequestVo.SignUp userVo){
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
