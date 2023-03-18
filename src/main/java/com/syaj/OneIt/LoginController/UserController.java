package com.syaj.OneIt.LoginController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginService.UserService;
import com.syaj.OneIt.LoginVo.UserVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user controller")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired UserService userService;
	
	@Operation(summary = "user register/modify method")
	@PostMapping(value = "/userRegister")
	public void userRegister(@RequestBody UserEntity user) {
		userService.userRegister(user);
	}
	
	@GetMapping(value = "/allUserSelect")
	public List<UserVo> allUserSelect() {
		return userService.allUserSelect();
	}
	
	@GetMapping(value = "/userSearch")
	public UserVo userSearch(@RequestParam Long id) {
		return userService.userSearch(id);
	}

	@DeleteMapping(value = "/userDel")
	public void userDel(@RequestParam Long id) {
		userService.userDel(id);
	}
	

}
