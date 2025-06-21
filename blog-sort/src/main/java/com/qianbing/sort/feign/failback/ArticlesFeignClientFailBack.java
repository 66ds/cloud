package com.qianbing.sort.feign.failback;

import com.qianbing.common.entity.LabelsEntity;
import com.qianbing.common.entity.SetArtitleSortEntity;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import com.qianbing.sort.feign.ArticleFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * FeignClient兜底回调，FailBack；
 * 可以返回缓存，假数据，默认数据等
 */
@Slf4j
@Component
public class ArticlesFeignClientFailBack implements ArticleFeignClient {

    @Override
    public List<SetArtitleSortEntity> getArticleIdsBySortId(Long sortId) {
        return List.of();
    }
}
