package com.project.basicboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Data Rest - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc){
        this.mvc = mvc;
    }


    // 리포지터리까지 실행시켜서 하이버네이트 쿼리까지 출력됨 -> db에 영향을 주는 테스트가 되어버림 -> 위에 @Transactional 어노테이션 추가
    // 모든 테스트 밑에 메소드들은 rollback 상태로 묶이게 됨
    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void test() throws Exception {
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
                //.andDo(print());
    }


    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void testArticleGet() throws Exception {
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void testArticleListGet() throws Exception {
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 단건 게시글 댓글 리스트 조회")
    @Test
    void testArticleCommentListGet() throws Exception {
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void testArticleCommentGet() throws Exception {
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        //.andDo(print());
    }

}
