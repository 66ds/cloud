package com.qianbing.common.vo;

import lombok.Data;


@Data
public class SearchParamVo {

    private String keyWord;//关键字查询

    private String sortName;//分类名称

    private String archivesTime;//归档时间

    private Integer page = 1;//页码

    private Integer limit = 10;//每页多少条

    private Integer userId;//用户Id

    private String labelName;//标签名字
}
