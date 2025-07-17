package com.qianbing.article.stategy.article;

import com.qianbing.common.entity.ArticlesEntity;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 按发布时间倒序
 */
@Component("date")
public class NewSortStrategy implements ArticleSortStrategy {
    @Override
    public List<ArticlesEntity> sort(List<ArticlesEntity> articles) {
        return articles.stream().sorted(
                Comparator.comparing(ArticlesEntity::getArticleDate)
                        .reversed()).collect(Collectors.toList());
    }
}
