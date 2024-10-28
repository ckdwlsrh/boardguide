package com.ckdwls.boardguide.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreWithBoardGameDTO {

    private int id;
    private String genre;
    private List<String> boardGames;

}
