package com.spring.mvc.chap05.dto.request;

import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    private String account;
    private String password;
    private boolean autoLogin;


}
