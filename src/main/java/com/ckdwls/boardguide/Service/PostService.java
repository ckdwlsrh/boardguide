package com.ckdwls.boardguide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ckdwls.boardguide.Entity.MatchingPosts;
import com.ckdwls.boardguide.Entity.User;
import com.ckdwls.boardguide.Persistence.BoardGameRepository;
import com.ckdwls.boardguide.Persistence.MatchingPostsRepository;

import java.util.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    
    @Autowired
    private final MatchingPostsRepository matchingPostsRepository;

    @Autowired
    private final BoardGameRepository boardGameRepository;

    private static class PriorityArray {
        private int priority;
        private double latitude;
        private double longitude;
        private MatchingPosts matchingPosts;

        public PriorityArray(int priority, double latitude, double longitude, MatchingPosts matchingPosts) {
            this.priority = priority;
            this.latitude = latitude;
            this.longitude = longitude;
            this.matchingPosts = matchingPosts;
        }
        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
        public MatchingPosts geMatchingPosts() {
            return matchingPosts;
        }

        public int getPriority() {
            return priority;
        }
    }

    public void create(MatchingPosts matchingPosts) {
        matchingPostsRepository.save(matchingPosts);
    }

    public void update(MatchingPosts matchingPosts) {
        Optional<MatchingPosts> optionalEntity = matchingPostsRepository.findById(matchingPosts.getId());
        if (optionalEntity.isPresent()) {
            MatchingPosts entity = optionalEntity.get();
            entity.setUserNum(matchingPosts.getUserNum());
            entity.setUsers(matchingPosts.getUsers());

            matchingPostsRepository.save(entity);  // save로 업데이트
        }
    }

    public void delete(MatchingPosts matchingPosts) {
        Optional<MatchingPosts> optionalEntity = matchingPostsRepository.findById(matchingPosts.getId());
        if (optionalEntity.isPresent()) {
            MatchingPosts entity = optionalEntity.get();
            matchingPostsRepository.deleteById(entity.getId());
        }
    }
    public List<PriorityArray> sortLocationsByPriorityAndDistance(double baseLat, double baseLon, List<PriorityArray> priorityArray) {
        // 우선순위로 먼저 정렬, 우선순위가 같으면 거리 기준으로 정렬
        Collections.sort(priorityArray, (a, b) -> {
            if (a.getPriority() != b.getPriority()) {
                return Integer.compare(b.getPriority(), a.getPriority()); // 우선순위 오름차순
            }
            // 우선순위가 같으면 거리 비교
            double distanceA = HaversineUtil.calculateDistance(baseLat, baseLon, a.getLatitude(), a.getLongitude());
            double distanceB = HaversineUtil.calculateDistance(baseLat, baseLon, b.getLatitude(), b.getLongitude());
            return Double.compare(distanceA, distanceB); // 거리 오름차순
        });

        return priorityArray;
    }
    
    public List<MatchingPosts> findMyPosts(String userId) {
        List<MatchingPosts> myPosts = matchingPostsRepository.findByUsersContaining(userId);
        return myPosts;
    }

    public List<MatchingPosts> findAllOrderByUser(User user) {

        double baseLan = Double.parseDouble(user.getLatitude());
        double baseLon = Double.parseDouble(user.getLongitude());

        List<MatchingPosts> matchingPosts = matchingPostsRepository.findAll();

        List<PriorityArray> priorityArray = new ArrayList<>();

        for (MatchingPosts matchingPost: matchingPosts) {
            int p = 0;
            String boardgame = matchingPost.getBoardgame();
            
            List<String> genres = boardGameRepository.findGenreByBoardGame(boardgame);


            for (String genre : user.getFavoriteGenre()) {
                if(genres.contains(genre)) p += 1;
            }
            
            double latitude = Double.parseDouble(matchingPost.getLatitude());
            double longitude = Double.parseDouble(matchingPost.getLongitude());

            priorityArray.add(new PriorityArray(p, latitude, longitude, matchingPost));

        }
        
        priorityArray = sortLocationsByPriorityAndDistance(baseLan, baseLon, priorityArray);

        List<MatchingPosts> sortedMatchingPosts = new ArrayList<>();
        for(PriorityArray p: priorityArray) {
            sortedMatchingPosts.add(p.geMatchingPosts());
        }

        return sortedMatchingPosts;
    }
}
