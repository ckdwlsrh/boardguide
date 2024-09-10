package com.ckdwls.boardguide.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ckdwls.boardguide.Entity.BoardGame;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long>{
    boolean existsByBoardGame(String boardGame);
    BoardGame findByBoardGame(String boardGame);
}
