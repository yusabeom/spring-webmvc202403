package com.spring.mvc.chap05.entity;

import lombok.*;

import java.time.LocalDateTime;

/*
*   # 회원 관리 테이블
CREATE TABLE tbl_member (
    account VARCHAR(50),
    password VARCHAR(150) NOT NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    auth VARCHAR(20) DEFAULT 'COMMON',
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_member PRIMARY KEY (account)
);
* */
@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private String account;
    private String password;
    private String name;
    private String email;
    private Auth auth;
    private LocalDateTime legDate;

}
