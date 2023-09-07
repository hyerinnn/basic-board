package com.project.basicboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleDto(
        LocalDateTime createAt,
        String createBy,
        String title,
        String content,
        String hashtag
) implements Serializable {

    public static ArticleDto of(LocalDateTime createAt, String createBy, String title, String content, String hashtag) {
        return new ArticleDto(createAt, createBy, title, content, hashtag);

    }
}
