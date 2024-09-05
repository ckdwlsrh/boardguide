package com.ckdwls.boardguide.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "ID가 올바르지 않습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 올바르지 않습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "이름이 올바르지 않습니다.")
    private String name;

}
