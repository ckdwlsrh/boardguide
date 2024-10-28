package com.ckdwls.boardguide.Controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ckdwls.boardguide.DTO.BoardGameDTO;
import com.ckdwls.boardguide.DTO.GenreWithBoardGameDTO;
import com.ckdwls.boardguide.DTO.MatchingPostsDTO;
import com.ckdwls.boardguide.DTO.ProfileDTO;
import com.ckdwls.boardguide.DTO.ResponseDTO;
import com.ckdwls.boardguide.Entity.BoardGame;
import com.ckdwls.boardguide.Entity.MatchingPosts;
import com.ckdwls.boardguide.Entity.User;
import com.ckdwls.boardguide.Persistence.BoardGameRepository;
import com.ckdwls.boardguide.Service.CrawlingBoardGame;
import com.ckdwls.boardguide.Service.PostService;
import com.ckdwls.boardguide.Service.UserService;
import com.ckdwls.boardguide.Util.DeduplicationUtils;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CrawlingBoardGame CrawlingBoardGame;

    @Autowired
    private BoardGameRepository boardGameRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping(value = "/data/crawling", method = RequestMethod.GET)
    public void CrawlingTest() {
        CrawlingBoardGame.BoardLife();
    }
    
    @RequestMapping(value = "/data/getGenre", method=RequestMethod.GET)
    public ResponseEntity<?> getGenre() {
        try {
            List<String> genres = boardGameRepository.findDistinctGenres();
            List<GenreWithBoardGameDTO> data = new ArrayList<>();
            int count = 0;
            for (String genre : genres) {
                List<String> boardGames = boardGameRepository.findTop10ByGenreOrderByRatingDesc(genre);
                data.add(GenreWithBoardGameDTO.builder().id(count).genre(genre).boardGames(boardGames).build());
                count += 1;
            }
            return ResponseEntity.ok().body(data);
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/matchingposts")
    public ResponseEntity<?> getMatchingPosts(@RequestBody MatchingPosts param) {
        try {
            String userId = param.getUserId();
            User user = userService.findByUser(userId);

            List<MatchingPosts> boardgame = postService.findAllOrderByUser(user);
            
            List<MatchingPostsDTO> datas = new ArrayList<>();

            for (MatchingPosts entity: boardgame) {
                MatchingPostsDTO dto = MatchingPostsDTO.builder().address(entity.getAddress()).userId(entity.getUserId()).id(entity.getId())
                .boardgame(entity.getBoardgame()).imageAddress(entity.getImageAddress()).nickname(entity.getNickname()).limitUser(entity.getLimitUser())
                .title(entity.getTitle()).userNum(entity.getUserNum()).latitude(entity.getLatitude()).longitude(entity.getLongitude()).limitDate(entity.getLimitDate())
                .users(entity.getUsers())
                .build();
                
                datas.add(dto);
            }

            return ResponseEntity.ok().body(datas);
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PostMapping("/myposts")
    public ResponseEntity<?> postMethodName(@RequestBody MatchingPosts param) {
        try{
            List<MatchingPosts> myPosts = postService.findMyPosts(param.getUserId());
            
            List<MatchingPostsDTO> datas = new ArrayList<>();

            for (MatchingPosts entity: myPosts) {
                MatchingPostsDTO dto = MatchingPostsDTO.builder().address(entity.getAddress()).userId(entity.getUserId()).id(entity.getId())
                .boardgame(entity.getBoardgame()).imageAddress(entity.getImageAddress()).nickname(entity.getNickname()).limitUser(entity.getLimitUser())
                .title(entity.getTitle()).userNum(entity.getUserNum()).latitude(entity.getLatitude()).longitude(entity.getLongitude()).limitDate(entity.getLimitDate())
                .users(entity.getUsers())
                .build();
                
                datas.add(dto);
            }

            return ResponseEntity.ok().body(datas);
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    

    @PostMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(@RequestBody ProfileDTO userId) {
        try {
            User user = userService.findByUser(userId.getUserId());

            ProfileDTO profileDTO = ProfileDTO.builder().email(user.getEmail()).address(user.getAddress())
            .favoriteGenre(user.getFavoriteGenre()).latitude(user.getLatitude()).longitude(user.getLongitude())
            .nickname(user.getNickname()).userId(user.getUserId()).build();

            return ResponseEntity.ok().body(profileDTO);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/boardgame")
    public ResponseEntity<?> boardList() {
        try{
            System.out.println("hello");
            List<BoardGame> boardGames = boardGameRepository.findAll();
            
            List<BoardGame> datas = DeduplicationUtils.deduplication(boardGames, BoardGame::getBoardGame);

            return ResponseEntity.ok().body(datas);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/posting")
    public ResponseEntity<?> posting(@RequestBody MatchingPosts param) {
        try{
            postService.create(param);
            return ResponseEntity.ok().body(param);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/updatepost")
    public ResponseEntity<?> updatePost(@RequestBody MatchingPosts entity) {
        try{
            postService.update(entity);
            return ResponseEntity.ok().body(entity);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/deletepost")
    public ResponseEntity<?> deletePost(@RequestBody MatchingPosts entity) {
        try{
            System.out.println(entity.toString());
            postService.delete(entity);
            return ResponseEntity.ok().body(entity);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    
    
}
