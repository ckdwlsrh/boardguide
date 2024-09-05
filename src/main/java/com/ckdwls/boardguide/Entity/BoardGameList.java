package com.ckdwls.boardguide.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGameList {
    private String boardGame;
    private String imageAddress;
    private String description;
    private String imageDescription;
    private int playingTime;
    private int players;
    private int age;
    private int weight;
}
