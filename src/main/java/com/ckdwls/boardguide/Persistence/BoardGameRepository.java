package com.ckdwls.boardguide.Persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ckdwls.boardguide.Entity.BoardGame;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long>{
    boolean existsByBoardGame(String boardGame);
    BoardGame findByBoardGame(String boardGame);
    List<BoardGame> findByGenre(String genre);


    @Query(value = "SELECT genre FROM board_game WHERE board_game = :boardGame", nativeQuery = true)
    List<String> findGenreByBoardGame(@Param("boardGame") String boardGame);

    @Query(value = "SELECT board_game FROM board_game WHERE genre = :genre and rating IS NOT NULL ORDER BY CAST(rating AS DECIMAL(10, 2)) DESC, id ASC LIMIT 10", nativeQuery = true)
    List<String> findTop10ByGenreOrderByRatingDesc(@Param("genre") String genre);
    
    @Query(value = "SELECT DISTINCT genre FROM board_game", nativeQuery = true)
    List<String> findDistinctGenres();

}