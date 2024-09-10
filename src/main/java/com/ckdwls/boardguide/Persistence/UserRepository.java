package com.ckdwls.boardguide.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ckdwls.boardguide.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    // 유저 아이디가 존재하면 true, 아니면 false
    boolean existsByUserId(String userId);
    boolean existsByNickname(String Nickname);
    // 유저 아이디값인 객체 반환
    User findByUserId(String userId);
}