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
public class ProfileDTO {
    private String userId;

    private String nickname;

    private String email;

    private String latitude;
    private String longitude;

    private String address;
    
    private List<String> favoriteGenre;
}
