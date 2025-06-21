package com.qianbing.search.service;

import com.alibaba.fastjson.JSON;
import com.qianbing.common.vo.SearchParamVo;
import com.qianbing.common.vo.SearchResultVo;
import com.qianbing.common.vo.SearchSaveVo;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class SearchServiceImpl implements SearchService{

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResultVo getArticlesList(SearchParamVo searchParamVo) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder filterBoolQueryBuilder = QueryBuilders.boolQuery();
        String keyWord = searchParamVo.getKeyWord();
        if(!StringUtils.isEmpty(keyWord)){
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("articleIntroduce",keyWord.toLowerCase()));
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("articleTitle",keyWord.toLowerCase()));
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("labelsEntities",keyWord.toLowerCase()));
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("sortNameNew",keyWord.toLowerCase()));
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("userNickname",keyWord.toLowerCase()));
            if(keyWord.matches("^[0-9]*$")){
                filterBoolQueryBuilder.should(QueryBuilders.termsQuery("articleViewsNew",keyWord.toLowerCase()));
                filterBoolQueryBuilder.should(QueryBuilders.termsQuery("articleCommentCountNew",keyWord.toLowerCase()));
                filterBoolQueryBuilder.should(QueryBuilders.termsQuery("articleLikeCountNew",keyWord.toLowerCase()));
            }
            filterBoolQueryBuilder.should(QueryBuilders.wildcardQuery("articleTitleNew",keyWord));
            filterBoolQueryBuilder.should(QueryBuilders.wildcardQuery("articleIntroduceNew",keyWord));
            filterBoolQueryBuilder.should(QueryBuilders.wildcardQuery("userNicknameNew",keyWord));
            boolQueryBuilder.filter(filterBoolQueryBuilder);
        }
        String labelName = searchParamVo.getLabelName();
        if(!StringUtils.isEmpty(labelName)){
            filterBoolQueryBuilder.should(QueryBuilders.termsQuery("labelsEntities",labelName.toLowerCase()));
            boolQueryBuilder.filter(filterBoolQueryBuilder);
        }
        if(!StringUtils.isEmpty(searchParamVo.getSortName())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("sortName",searchParamVo.getSortName()));
        }
        if(!StringUtils.isEmpty(searchParamVo.getArchivesTime())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("articleDate",searchParamVo.getArchivesTime()));
        }
        if(!StringUtils.isEmpty(searchParamVo.getUserId())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery("userId",new int[]{searchParamVo.getUserId()}));
        }
        boolQueryBuilder.filter(QueryBuilders.termsQuery("isDelete",new int[]{0}));//取未删除的
        searchSourceBuilder.query(boolQueryBuilder);
        //排序(按照查看人数排序)
        searchSourceBuilder.sort("articleViews", SortOrder.DESC);
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("articleTitle")
                .field("articleIntroduce")
                .field("sortNameNew")
                .field("labelsEntities")
                .field("userNickname")
                .field("articleViewsNew")
                .field("articleCommentCountNew")
                .field("articleLikeCountNew")
                .field("articleTitleNew")
                .field("articleIntroduceNew")
                .field("userNicknameNew")
                .preTags("<b style='color:red'>").postTags("</b>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //数据进行分页
        searchSourceBuilder.from((searchParamVo.getPage()-1)*searchParamVo.getLimit());
        searchSourceBuilder.size(searchParamVo.getLimit());
        //整合分析
//        TermsAggregationBuilder sort_agg = AggregationBuilders.terms("sort_name_agg").field("sortName").size(99999);
        TermsAggregationBuilder label_agg = AggregationBuilders.terms("date_agg").field("articleDate").size(99999);
//        searchSourceBuilder.aggregation(sort_agg);
        searchSourceBuilder.aggregation(label_agg);
        SearchRequest searchRequest = new SearchRequest(new String[]{"blog"},searchSourceBuilder);
//        System.out.println(searchSourceBuilder.toString());
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            //e.printStackTrace();
            log.error(e.getMessage());
        }
        SearchResultVo searchResultVo = new SearchResultVo();
        if(searchResponse != null){
            List<SearchSaveVo> searchSaveVoList = new ArrayList<>();
            SearchHit[] hits = searchResponse.getHits().getHits();
            Set<String> sortNames = new HashSet<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                SearchSaveVo searchSaveVo = JSON.parseObject(sourceAsString, SearchSaveVo.class);

                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleTitle"))){
                    Text[] articleTitles = hit.getHighlightFields().get("articleTitle").getFragments();
                    searchSaveVo.setArticleTitle(articleTitles[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleIntroduce"))){
                    Text[] articleIntroduce = hit.getHighlightFields().get("articleIntroduce").getFragments();
                    searchSaveVo.setArticleIntroduce(articleIntroduce[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("sortNameNew"))){
                    Text[] sortName = hit.getHighlightFields().get("sortNameNew").getFragments();
                    searchSaveVo.setSortNameNew(sortName[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("labelsEntities"))){
                    Text[] labelsEntities = hit.getHighlightFields().get("labelsEntities").getFragments();
                    searchSaveVo.setLabelsEntities(labelsEntities[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("userNickname"))){
                    Text[] userNickname = hit.getHighlightFields().get("userNickname").getFragments();
                    searchSaveVo.setUserNickname(userNickname[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleViewsNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("articleViewsNew").getFragments();
                    searchSaveVo.setArticleViewsNew(userNickname[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleCommentCountNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("articleCommentCountNew").getFragments();
                    searchSaveVo.setArticleCommentCountNew(userNickname[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleLikeCountNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("articleLikeCountNew").getFragments();
                    searchSaveVo.setArticleLikeCountNew(userNickname[0].toString());
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleTitleNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("articleTitleNew").getFragments();
                    searchSaveVo.setArticleTitleNew(userNickname[0].toString());
                    searchSaveVo.setIsTitleHigh(1);
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("articleIntroduceNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("articleIntroduceNew").getFragments();
                    searchSaveVo.setArticleIntroduceNew(userNickname[0].toString());
                    searchSaveVo.setIsIntroduceHigh(1);
                }
                if(!StringUtils.isEmpty(hit.getHighlightFields().get("userNicknameNew"))){
                    Text[] userNickname = hit.getHighlightFields().get("userNicknameNew").getFragments();
                    searchSaveVo.setUserNicknameNew(userNickname[0].toString());
                    searchSaveVo.setIsNameHigh(1);
                }
                sortNames.add(searchSaveVo.getSortName());
                searchSaveVoList.add(searchSaveVo);
            }
            searchResultVo.setSearchSaveVos(searchSaveVoList);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms dateTerms = aggregations.get("date_agg");
//        Terms sortTerms = aggregations.get("sort_name_agg");
            List<? extends Terms.Bucket> dateTermsBuckets = dateTerms.getBuckets();
//        List<? extends Terms.Bucket> sortTermsBuckets = sortTerms.getBuckets();
            Set<String> archivesTimes = new HashSet<>();

            for (Terms.Bucket bucket : dateTermsBuckets) {
                String keyAsString = bucket.getKeyAsString();
                archivesTimes.add(keyAsString);
            }
//        for (Terms.Bucket bucket : sortTermsBuckets) {
//            String keyAsString = bucket.getKeyAsString();
//            sortNames.add(keyAsString);
//        }
            searchResultVo.setArchivesTimes(archivesTimes);
            searchResultVo.setSortNames(sortNames);
            searchResultVo.setPageTotal(searchResponse.getHits().getTotalHits().value);
        }
        return searchResultVo;
    }
}
