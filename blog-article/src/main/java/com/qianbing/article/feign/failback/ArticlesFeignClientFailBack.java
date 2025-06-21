package com.qianbing.article.feign.failback;

import com.qianbing.article.feign.SearchFeignClient;
import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * FeignClient兜底回调，FailBack；
 * 可以返回缓存，假数据，默认数据等
 */
@Slf4j
@Component
public class ArticlesFeignClientFailBack implements SearchFeignClient {
    @Override
    public SearchResultVo getArticlesList(SearchParamVo searchParamVo) {
        log.info("调用blog-search服务出现问题");
        SearchResultVo searchResultVo = new SearchResultVo();
        return searchResultVo;
    }

    @Override
    public List<LabelsEntity> getLabelEntityByIds(List<Long> labelIds) {
        return List.of();
    }
}
