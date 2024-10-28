package com.ckdwls.boardguide.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchingPosts {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String title;
    
    private String boardgame;
    private String userId;
    private String imageAddress;
    private String nickname;
    private int userNum;

    @ElementCollection
    private List<String> users;

    private String limitUser;
    private String limitDate;
    private String latitude;
    private String longitude;
    private String address;

}