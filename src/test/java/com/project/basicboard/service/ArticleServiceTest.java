package com.project.basicboard.service;

import com.project.basicboard.domain.Article;
import com.project.basicboard.domain.eum.SearchType;
import com.project.basicboard.dto.ArticleDto;
import com.project.basicboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.BDDMockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks  private ArticleService sut;
    @Mock private ArticleRepository articleRepository;


    @DisplayName("게시글 검색 시, 게시글 리스트를 반환")
    @Test
    void givenSearchParam_whenSearchingArticles_thenReturnArticles(){
        // given
        //SearchParameters param = SearchParameters.of(type, "search keyword");

        //when
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");

        //then
        assertThat(articles).isNotNull();
    }


    @DisplayName("게시글 조회하면 게시글 반환")
    @Test
    void givenArticleId_whenSearchArticle_thenReturnArticle(){
        // given
        //SearchParameters param = SearchParameters.of(type, "search keyword");

        //when
        ArticleDto article = sut.searchArticle(1L);

        //then
        assertThat(article).isNotNull();
    }


    @DisplayName("게시글 생성")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSaveArticle(){
        // given
        //SearchParameters param = SearchParameters.of(type, "search keyword");
        ArticleDto dto = ArticleDto.of(LocalDateTime.now(), "hyerin", "title",  "content",  "hashtag");
        willDoNothing().given(articleRepository).save(any(Article.class));


        //when
        sut.saveArticle(dto);

        //then
        then(articleRepository).should().save(any(Article.class));
    }


}