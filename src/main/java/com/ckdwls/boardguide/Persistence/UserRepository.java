package com.ckdwls.boardguide.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ckdwls.boardguide.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    // 유저 아이디가 존재하면 true, 아니면 false
    boolean existsByUserId(String userId);
    // 유저 아이디값인 객체 반환
    User findByUserId(String userId);
}