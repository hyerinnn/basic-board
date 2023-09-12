package com.project.basicboard.repository;

import com.project.basicboard.domain.Article;
import com.project.basicboard.domain.QArticle;
import com.project.basicboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        //Article에 대한 모든 검색기능을 추가해줌
        QuerydslPredicateExecutor<Article>
        ,QuerydslBinderCustomizer<QArticle>
{

    //Containing은 부분검색이 가능
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);


    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        //선택적 검색을 위함
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.hashtag, root.content, root.id, root.createdAt, root.createdBy);
        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  // like '${v}'   -> %을 안쓰고 검색할때.
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);  // like '%${v}%'  -> 일반적인 contains 기능
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
}