package com.project.basicboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map){

        map.addAttribute("articles", List.of());

        return "articles/index";
    }


    @GetMapping("/{aricleId}")
    public String article(@PathVariable Long aricleId,ModelMap map){

        map.addAttribute("article","article");
        map.addAttribute("articleComments", List.of());


        return "articles/detail";
    }
}
