package com.ckdwls.boardguide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ckdwls.boardguide.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.ckdwls.boardguide.Persistence.BoardGameRepository;
import com.ckdwls.boardguide.Persistence.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BoardGameRepository boardGameRepository;

    @Value("${crawling.images}")
    private String destinationDir;

    public boolean checkUserIdDuplicated(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public User findByUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public User Signup(final User user) {
        final String userId = user.getUserId();
        final String nickname = user.getNickname();
        //favoriteGenre
        final List<String> favoriteGenreIndex = user.getFavoriteGenre();

        List<String> genres = boardGameRepository.findDistinctGenres();
        
        List<String> favoriteGenre = new ArrayList<>();

        for ( String i : favoriteGenreIndex) {
            favoriteGenre.add(genres.get(Integer.parseInt(i)));
        }
        user.setFavoriteGenre(favoriteGenre);

        if(user == null || userId == null) {
            throw new RuntimeException("Invalid arguments");
        }
        if(userRepository.existsByUserId(userId)) {
            throw new RuntimeException("UserId already exists");
        }
        if(userRepository.existsByNickname(nickname)) {
            throw new RuntimeException("Nickname already exists");
        }

        return userRepository.save(user);
    }

    public User Signin(final String userId, final String password, final PasswordEncoder encoder) {
        User user = userRepository.findByUserId(userId);
        // id가 없을때
        if(user == null) {
            return null;
        }
        // password가 맞지 않을때
        if(!encoder.matches(password, user.getPassword())){
            return null;
        }

        return user;
    }

    public void copyProfileImage(String nickname) throws IOException {
        // resources/static 경로의 파일 접근 (빌드 시 resources는 target/classes/static으로 복사됨)
        ClassPathResource resource = new ClassPathResource("static/" + "tempProfileImage.jpg");

        // 복사할 대상 디렉토리 및 파일 경로
        Path destinationPath = Paths.get(destinationDir, nickname + ".jpg");

        // 대상 디렉토리가 없는 경우 생성
        if (!Files.exists(destinationPath.getParent())) {
            Files.createDirectories(destinationPath.getParent());
        }

        // 파일을 InputStream으로 읽고, 새로운 경로에 복사
        try (InputStream inputStream = resource.getInputStream()) {
            Files.copy(inputStream, destinationPath);
            System.out.println("File copied successfully to: " + destinationPath);
        } catch (IOException e) {
            System.out.println("Error occurred while copying file: " + e.getMessage());
            throw e;
        }
    }
}
