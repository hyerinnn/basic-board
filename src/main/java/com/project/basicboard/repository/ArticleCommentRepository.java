package com.project.basicboard.repository;

import com.project.basicboard.domain.Article;
import com.project.basicboard.domain.ArticleComment;
import com.project.basicboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<Article>
        , QuerydslBinderCustomizer<QArticle>
{



    // _ 역할 (스프링 data jpa 공식문서에도 나옴)
    //게시글로 댓글을 추출해야 하는 경우
    // find에서 _를 쓰면,  Article의 id를 조회하겠다는 의미.
    List<ArticleComment> findByArticle_Id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        //선택적 검색을 위함
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content,root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }

}