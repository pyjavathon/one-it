package com.syaj.OneIt.UserService.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.syaj.OneIt.UserEntity.UserEntity;
import com.syaj.OneIt.UserRepository.UserRepository;
import com.syaj.OneIt.UserService.UserService;
import com.syaj.OneIt.UserVo.UserVo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired UserRepository userRepo;
	
	@Autowired ModelMapper modelMapper;
	
	@Override
	public void userRegister(UserEntity user) {
		userRepo.save(user);
	}

	@Override
	public List<UserVo> allUserSelect() {
		List<UserEntity> selectList = userRepo.findAll();
		
		//List<UserVo> resultList = Arrays.asList(modelMapper.map(search,UserVo[].class));
		
		List<UserVo> resultList = selectList.stream().map(m -> modelMapper.map(m, UserVo.class)).collect(Collectors.toList());
		
		return resultList;
	}

	@Override
	public UserVo userSearch(Long id) {
		Optional<UserEntity> user = userRepo.findById(id); 
		
		if(!user.isPresent())  throw new NotFoundException("id와 일치하는 회원이 없습니다.");
			
		
		UserVo searchResult =  modelMapper.map(user,UserVo.class);
		return searchResult;
	}

	@Override
	public void userDel(Long id) {
		userRepo.deleteById(id);
		
	}

}
