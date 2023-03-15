package com.syaj.OneIt.LoginRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.syaj.OneIt.LoginEntity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	@EntityGraph(attributePaths = "authorities")//쿼리가 수행될 때 Lazy 조회가 아니고  Eager 조회로 authorities 정보를 같이 가져옴
	Optional<UserEntity> findOneWithAuthoritiesByUserEmail(String userEmail);

}
