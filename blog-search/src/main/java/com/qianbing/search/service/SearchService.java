package com.qianbing.search.service;

import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;

public interface SearchService {
    SearchResultVo getArticlesList(SearchParamVo searchParamVo);
}
