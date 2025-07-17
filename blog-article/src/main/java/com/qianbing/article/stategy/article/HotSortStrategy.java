package com.qianbing.article.stategy.article;

import com.qianbing.common.entity.ArticlesEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 热度排序（点赞数）
 */
@Component("hot")
public class HotSortStrategy implements ArticleSortStrategy {
    @Override
    public List<ArticlesEntity> sort(List<ArticlesEntity> articles) {
        return articles.stream().sorted(
                Comparator.comparingLong(ArticlesEntity::getArticleLikeCount)
                        .reversed()).collect(Collectors.toList());
    }
}
