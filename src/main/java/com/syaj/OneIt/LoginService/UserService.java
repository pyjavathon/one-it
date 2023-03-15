package com.syaj.OneIt.LoginService;

import java.util.List;
import java.util.Optional;

import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginVo.UserVo;

public interface UserService {
	
	public void userRegister(UserEntity user);

	public List<UserVo> allUserSelect();

	public UserVo userSearch(Long id);

	public void userDel(Long id);

	UserEntity signup(UserVo userVo);

	Optional<UserEntity> getUserWithAuthorities(String userEmail);

	Optional<UserEntity> getMyUserWithAuthorities();

}
