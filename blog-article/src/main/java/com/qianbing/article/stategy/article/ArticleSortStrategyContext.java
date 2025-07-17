package com.qianbing.article.stategy.article;

import com.qianbing.common.entity.ArticlesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ArticleSortStrategyContext {

    @Autowired
    private Map<String, ArticleSortStrategy> strategyMap;

    public List<ArticlesEntity> sort(String type, List<ArticlesEntity> articles) {
        ArticleSortStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            // 默认按时间排序
            strategy = strategyMap.get("new");
        }
        return strategy.sort(articles);
    }
}
