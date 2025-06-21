package com.qianbing.search.controller;

import com.qianbing.search.service.SearchService;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    /**
     * 博客前台(elasticsearch-查所有文章内容信息)
     */
    @RequestMapping("/getArticlesList")
    public SearchResultVo getArticlesList(@RequestBody SearchParamVo searchParamVo) {
        SearchResultVo searchResultVo = searchService.getArticlesList(searchParamVo);
        return searchResultVo;
    }
}
