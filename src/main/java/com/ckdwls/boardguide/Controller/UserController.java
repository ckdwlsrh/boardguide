package com.ckdwls.boardguide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ckdwls.boardguide.DTO.ResponseDTO;
import com.ckdwls.boardguide.DTO.SigninRequest;
import com.ckdwls.boardguide.DTO.SignupRequest;
import com.ckdwls.boardguide.Entity.User;
import com.ckdwls.boardguide.Service.TokenProvider;
import com.ckdwls.boardguide.Service.UserService;


@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> signin(@RequestBody SigninRequest param) {
        try {
            User user = userService.Signin(param.getUserId(), param.getPassword(), passwordEncoder);

            // 없거나 불일치
            if(user == null) {
                throw new Exception("User doesn't exist");
            }
            //토큰 생성
            final String token = tokenProvider.createToken(user);
            SigninRequest responseSigninRequest = SigninRequest.builder()
                                                               .token(token)
                                                               .userId(user.getUserId())
                                                               .build();

            return ResponseEntity.ok().body(responseSigninRequest);
        }
        catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> singup(@RequestBody SignupRequest param) {
        try {
            //패스워드 확인
            if(param.getPassword().equals(param.getPasswordCheck())) {
                throw new Exception("Password is not correct.");
            }
            //엔티티로 바꿔서 등록
            User user = User.builder()
                            .userId(param.getUserId())
                            .password(passwordEncoder.encode(param.getPassword()))
                            .name(param.getName())
                            .build();

            User registeduser = userService.Signup(user);
            //응답DTO작성
            SignupRequest responseSignupRequest = SignupRequest.builder()
                                                                .userId(registeduser.getUserId())
                                                                .name(registeduser.getName())
                                                                .build();

            return ResponseEntity.ok().body(responseSignupRequest);
        }
        catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    
}
