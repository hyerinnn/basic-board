package com.project.basicboard.service;

import com.project.basicboard.domain.Article;
import com.project.basicboard.domain.eum.SearchType;
import com.project.basicboard.dto.ArticleDto;
import com.project.basicboard.dto.ArticleWithCommentsDto;
import com.project.basicboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable)
                    //ArticleDto로 바꿔줌  -> ArticleDto 내의 from 메소드를 사용하겠다.
                    .map(ArticleDto :: from);  // dto로 변환해서 리턴해줌으로써, Article 엔티티 자체는 외부에 드러나지 않도록 할 수 있다.
        }

        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByHashtag("#"+searchKeyword, pageable).map(ArticleDto::from);  //TODO : #입력시 #이 두번 들어갈수 있음 (리팩토링)
        };

        //return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                 // Optional이기때문에 바로 get하면 안되고 까줘야함.
                //orElseThrow : 문제가 생기면 다음처럼 처리하겠다.
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 -> articleId: "+articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {

        /**
         * [getReferenceById  vs  findById]
         * update할때 업데이트 할 게시물의 id를 알고 있는 경우, 수정후 저장하는 마지막 쿼리만 날리면 효율적.
         *
         * 수정할 엔티티를 findById로 영속성컨텍스트에서 가져와야하는데 여기서 셀렉트 쿼리가 발생한다. 이때 셀렉트 쿼리를 발생시키지 않으면 좋겠다 해서
         * 나온 게  getReferenceById 이다.
         *
         * */

        try {
            Article article = articleRepository.getReferenceById(dto.id());

            if(dto.title() != null ){
                article.setTitle(dto.title());
            }
            if(dto.content() != null ){
                article.setContent(dto.content());
            }
            article.setHashtag(dto.hashtag());

            // 변경된 것을 감지해서 수정하므로 save 굳이 쓸 필요 없음!!!!

        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패 -> 게시글을 찾을 수 없음 - dto: {}", dto);
        }

        /**
         * [자바 11, 12에서 새로 도입된 부분]
         * 보통 dto.getTitle() 이런식으로 dto에서 getter를 쓰는데, ArticleDto를 class가 아닌 record로 만들었고.
         * record는 getter,setter를 기본적으로 내장하고 있음(롬복없이)!!   그래서 dto.id() 이런식으로 쓸수 있는것
         * */

    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
