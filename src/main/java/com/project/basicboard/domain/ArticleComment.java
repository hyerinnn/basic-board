package com.project.basicboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article;    //게시글

    @Setter
    @Column(nullable = false, length = 500)
    private String content;     //내용



    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createAt;     //생성일

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createBy;            //생성자

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   //수정일

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;          //수정자


    // 룸북의 NoArgsConstructor 애노테이션 사용하면 이거 필요없음
    protected ArticleComment() {}


    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return  new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
