package com.project.basicboard.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
})
@Entity
//@EqualsAndHashCode
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;       //제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content;     //내용

    @Setter
    private String hashtag;     //해시태그

    // 이 article에 연동되어 있는 댓글은 중복을 허용하지 않고 다 모아서 컬렉션 리스트로 보겠다.
    // 실 운영에서 casecade는 안하기도 함(게시글은 사라져도 댓글은 가지고 있고 싶을 수 있으므로.)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("id")
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

/*


    // CreatedDate/CreatedBy  -> EnableJpaAuditing : jpa auditing 기능
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



*/




    // 모든 JAP 엔티티들은 기본 생성자가 필요함
    protected Article() {
    }

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // new 키워드를 쓰지 않고 쓸수있게.  도메인 article을 생성할땐 어떤 값을 필요로 하는지 이걸로 가이드 함.
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    // 동일성, 동등성 검사용도
    // 상단에 EqualsAndHashCode로 처리하면 모든 필드를 대상으로 검사한다. id로만 검사하면 되므로 아래처럼 직접 코드를 작성.
    // generate에서 equals & hashcode로 생성
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        // id가 당연히 not null 이라고 보고 id.equals를 함 ->
        // id도 null일 수도 있으니(db에 아직 들어가지 않았을때(영속화 되지않은 경우)) 를 체크해주기 위해 null체크.
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

