package com.syaj.OneIt.LoginService.Impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syaj.OneIt.LoginEntity.AuthorityEntity;
import com.syaj.OneIt.LoginEntity.UserEntity;
import com.syaj.OneIt.LoginRepository.UserRepository;
import com.syaj.OneIt.LoginService.UserService;
import com.syaj.OneIt.LoginVo.UserRequestVo;
import com.syaj.OneIt.LoginVo.UserRequestVo.Login;
import com.syaj.OneIt.jwt.JwtAuthenticationFilter;
import com.syaj.OneIt.jwt.TokenProvider;
import com.syaj.OneIt.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService  {
	
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private RedisTemplate redisTemplate;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public void userRegister(UserEntity user) {
		userRepo.save(user);
	}

	@Override
	public List<UserRequestVo.Login> allUserSelect() {
		List<UserEntity> selectList = userRepo.findAll();

		// List<UserVo> resultList =
		// Arrays.asList(modelMapper.map(search,UserVo[].class));

		List<UserRequestVo.Login> resultList = selectList.stream()
				.map(m -> modelMapper.map(m, UserRequestVo.Login.class)).collect(Collectors.toList());

		return resultList;
	}

	@Override
	public UserRequestVo.Login userSearch(Long id) {
		Optional<UserEntity> user = userRepo.findById(id);

		if (!user.isPresent())
			throw new NotFoundException("id와 일치하는 회원이 없습니다.");

		UserRequestVo.Login searchResult = modelMapper.map(user, UserRequestVo.Login.class);
		return searchResult;
	}

	@Override
	public void userDel(Long id) {
		userRepo.deleteById(id);

	}

	

	@Override
	@Transactional
	public UserEntity signup(UserRequestVo.SignUp userVo) {
		if (userRepo.findOneWithAuthoritiesByUserEmail(userVo.getUserEmail()).orElse(null) != null) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		}

		AuthorityEntity authority = AuthorityEntity.builder().authorityName("ROLE_USER").build();

		UserEntity user = UserEntity.builder()
									.userName(userVo.getUserName())
									.userPwd(passwordEncoder.encode(userVo.getUserPwd()))
									.userEmail(userVo.getUserEmail())
									.userBirth(userVo.getUserBirth())
									.authorities(Collections.singleton(authority))
									.userPhone(userVo.getUserPhone())
									.agreement(userVo.getAgreement())
									.activated(true).build();

		return userRepo.save(user);

	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> getUserWithAuthorities(String userEmail) {
		return userRepo.findOneWithAuthoritiesByUserEmail(userEmail);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> getMyUserWithAuthorities() {
		return SecurityUtil.getCurrentUsername().flatMap(userRepo::findOneWithAuthoritiesByUserEmail);
	}

	@Override
	public ResponseEntity<?> logout(UserRequestVo.Logout userRequestVo) {

		if (!tokenProvider.validateToken(userRequestVo.getAccesstoken()))
			return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");

		Authentication authentication = tokenProvider.getAuthentication(userRequestVo.getAccesstoken());

		if (redisTemplate.opsForValue().get("RT: " + authentication.getName()) != null) {
			redisTemplate.delete("RT: " + authentication.getName());
		}
		Long expiration = tokenProvider.getExpiration(userRequestVo.getAccesstoken());

		redisTemplate.opsForValue().set(userRequestVo.getAccesstoken(), "logout", expiration, TimeUnit.MILLISECONDS);

		return ResponseEntity.ok("로그아웃 되었습니다.");
	}

	@Override
	public ResponseEntity<?> login(Login userRequestVo) {
		if(userRepo.findByUserEmail(userRequestVo.getUserEmail()).orElse(null)==null) return ResponseEntity.badRequest().body("해당하는 회원이 없습니다.");
			
	    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userRequestVo.getUserEmail(), userRequestVo.getUserPwd());
	    Authentication authentication = null;
	    UserRequestVo.Logout jwt = null;
	    try {
		 authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
	    }catch(Exception e) {
	    	return ResponseEntity.badRequest().body("로그인 실패 (비밀번호가 일치하지 않습니다.)");
	    }
	    
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		try {
		jwt = tokenProvider.createToken(authentication);
		
		ObjectMapper mapper = new ObjectMapper();
		
        String tokenString = mapper.writeValueAsString(jwt);
        
        redisTemplate.opsForValue().set("RT: " + authentication.getName(), tokenString, jwt.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
		
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e+"로그인 실패(관리자에게 문의)");
		}
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		return ResponseEntity.ok(jwt);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String userEmail) {
		return userRepo.findOneWithAuthoritiesByUserEmail(userEmail).map(user -> createUser(userEmail, user))
				.orElseThrow(() -> new UsernameNotFoundException(userEmail + " -> DB에서 찾을 수 없습니다."));
	}

	private org.springframework.security.core.userdetails.User createUser(String userEmail, UserEntity user) {
		if (!user.isActivated()) {
			throw new RuntimeException(userEmail + " -> 활성화되어 있지 않습니다.");
		}

		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPwd(),
				grantedAuthorities);
	}



}
