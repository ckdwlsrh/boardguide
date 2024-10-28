package com.ckdwls.boardguide.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class BoardGameDTO {
    
    private String boardGame;
    private Long age;
    private Long playerMin;
    private Long playerMax;
    private Long playingTime;

    public BoardGameDTO(String boardGame, Long age, Long playerMin, Long playerMax, Long playingTime) {
        this.boardGame = boardGame;
        this.age = age;
        this.playerMin = playerMin;
        this.playerMax = playerMax;
        this.playingTime = playingTime;
    }
}
