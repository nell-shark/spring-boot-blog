package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.ArticleNotFoundException;
import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllArticles() {
        log.info("find all articles");
        return articleRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Article::getDate).reversed())
                .toList();
    }

    public Article findById(Long id) {
        log.info("find article by id: " + id);
        return articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFoundException("Article with id = %s not found".formatted(id)));
    }

    public void save(Article article) {
        log.info("save article: " + article.toString());
        articleRepository.save(article);
    }
}
