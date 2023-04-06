package com.syaj.OneIt.LoginService;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginVo.UserRequestVo;

public interface UserService {
	
	public void userRegister(UserEntity user);

	public List<UserRequestVo.Login> allUserSelect();

	public UserRequestVo.Login userSearch(Long id);

	public void userDel(Long id);

	UserEntity signup(UserRequestVo.SignUp userVo);

	Optional<UserEntity> getUserWithAuthorities(String userEmail);

	Optional<UserEntity> getMyUserWithAuthorities();

	public Object logout(UserRequestVo.Logout userRequestVo);

	public ResponseEntity<?> login(UserRequestVo.Login userRequestVo);

}
