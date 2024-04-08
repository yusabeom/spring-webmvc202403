package com.spring.mvc.chap05.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply {

    private int replyNo;
    @Setter
    private String replyText;
    @Setter
    private String replyWriter;
    private LocalDateTime replyDate;
    private int boardNo;
    private LocalDateTime updateDate;
    @Setter
    private String account;

    private String profileImage;


}
