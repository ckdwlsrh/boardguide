package com.ckdwls.boardguide.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ckdwls.boardguide.Entity.MatchingPosts;
import java.util.List;


@Repository
public interface MatchingPostsRepository extends JpaRepository<MatchingPosts,Long>{
    List<MatchingPosts> findByUserId(String userId);
    
    List<MatchingPosts> findByTitle(String title);
    List<MatchingPosts> findByUsersContaining(String userId);
}
