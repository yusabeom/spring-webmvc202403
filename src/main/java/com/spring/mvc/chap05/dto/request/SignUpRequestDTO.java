package com.spring.mvc.chap05.dto.request;

import com.spring.mvc.chap05.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {

    @NotBlank
    @Size(min = 4, max = 14)
    private String account;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 2, max = 6)
    private String name;

    @NotBlank
    @Email // 이메일 형식을 검증해주는 아노테이션
    private String email;

    // 프로필 사진 파일
    private MultipartFile profileImage;

    private Member.LoginMethod loginMethod;

    // dto 를 entity 로 변환하는 유틸메서드
    public Member toEntity(PasswordEncoder encoder, String savePath) {
        return Member.builder()
                .account(account)
                .password(encoder.encode(password))
                .name(name)
                .email(email)
                .profileImage(savePath)
                .loginMethod(loginMethod)
                .build();
    }


}
