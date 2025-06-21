package com.qianbing.common.vo;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Data
public class SearchResultVo {

    private List<SearchSaveVo> searchSaveVos = new ArrayList<>();//文章数据

    private Long pageTotal;//总记录数

    private Integer totalPages;//总页码

    private Set<String> sortNames = new HashSet<>(); //聚合分类

    private Set<String> archivesTimes =  new HashSet<>();//聚合归档时间

}
