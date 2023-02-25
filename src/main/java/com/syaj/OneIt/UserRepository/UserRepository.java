package com.syaj.OneIt.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.syaj.OneIt.UserEntity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

}
