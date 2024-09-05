package com.ckdwls.boardguide.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ckdwls.boardguide.Entity.User;
import com.ckdwls.boardguide.Persistence.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public boolean checkUserIdDuplicated(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public User Signup(final User user) {
        final String userId = user.getUserId();
        if(user == null || userId == null) {
            throw new RuntimeException("Invalid arguments");
        }
        if(userRepository.existsByUserId(userId)) {
            throw new RuntimeException("UserId already exists");
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

}
