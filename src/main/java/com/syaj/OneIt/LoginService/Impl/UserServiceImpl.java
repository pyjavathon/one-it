package com.syaj.OneIt.LoginService.Impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import com.syaj.OneIt.LoginEntity.AuthorityEntity;
import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginRepository.UserRepository;
import com.syaj.OneIt.LoginService.UserService;
import com.syaj.OneIt.LoginVo.UserVo;
import com.syaj.OneIt.util.SecurityUtil;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

	
	private final PasswordEncoder passwordEncoder;
	
	
	public UserServiceImpl(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	
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
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String userEmail) {
		return userRepo.findOneWithAuthoritiesByUserEmail(userEmail)
							 .map(user-> createUser(userEmail, user))
							 .orElseThrow(()->new UsernameNotFoundException(userEmail+" -> DB에서 찾을 수 없습니다."));
	}
	
	private org.springframework.security.core.userdetails.User createUser(String userEmail, UserEntity user) {
	      if (!user.isActivated()) {
	         throw new RuntimeException(userEmail + " -> 활성화되어 있지 않습니다.");
	      }
	      
	      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
	              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
	              .collect(Collectors.toList());

	      return new org.springframework.security.core.userdetails.User(user.getUserEmail(),
	              user.getUserPwd(),
	              grantedAuthorities);
	   }
	@Override
	@Transactional
	public UserEntity signup(UserVo userVo) {
		if(userRepo.findOneWithAuthoritiesByUserEmail(userVo.getUserEmail()).orElse(null) != null){
			throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		}
		
		AuthorityEntity authority = AuthorityEntity.builder()
									   .authorityName("ROLE_USER")
									   .build();
		
		UserEntity user = UserEntity.builder()
						.userName(userVo.getUserName())
						.userPwd(passwordEncoder.encode(userVo.getUserPwd()))
						.userEmail(userVo.getUserEmail())
						.userBirth(userVo.getUserBirth())
						.authorities(Collections.singleton(authority))
						.userPhone(userVo.getUserPhone())
						.agreement(userVo.getAgreement())
						.activated(true)
						.build();
		
		return userRepo.save(user);
						
	}
	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> getUserWithAuthorities(String userEmail){
		return userRepo.findOneWithAuthoritiesByUserEmail(userEmail);
	}
	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> getMyUserWithAuthorities(){
		return SecurityUtil.getCurrentUsername().flatMap(userRepo::findOneWithAuthoritiesByUserEmail);
	}
	

}
