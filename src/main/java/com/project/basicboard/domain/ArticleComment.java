package com.project.basicboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {

    private Long id;
    private Article article;    //게시글
    private String content;     //내용

    private LocalDateTime createAt;     // 생성일
    private String createBy;            // 생성자
    private LocalDateTime modifiedAt;   //수정일
    private String modifiedBy;          //수정자
}
