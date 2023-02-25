package com.syaj.OneIt.UserService;

import java.util.List;

import com.syaj.OneIt.UserEntity.UserEntity;
import com.syaj.OneIt.UserVo.UserVo;

public interface UserService {
	
	public void userRegister(UserEntity user);

	public List<UserVo> allUserSelect();

	public UserVo userSearch(Long id);

	public void userDel(Long id);

}
