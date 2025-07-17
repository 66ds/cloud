package com.qianbing.article.stategy.article;

import com.qianbing.common.entity.ArticlesEntity;

import java.util.List;

/**
 * 文章排序策略
 */
public interface ArticleSortStrategy {
        List<ArticlesEntity> sort(List<ArticlesEntity> articles);
}
