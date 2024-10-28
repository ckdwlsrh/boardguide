package com.ckdwls.boardguide.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingPostsDTO {
    private Long id;
    private String userId;
    private String boardgame;
    private String title;
    private String nickname;
    private int userNum;
    private String address;
    private String imageAddress;
    private String limitUser;

    private String limitDate;
    private List<String> users;
    private String latitude;
    private String longitude;
}
