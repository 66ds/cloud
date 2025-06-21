package com.qianbing.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchSaveVo {

    private Long articleId;

    private String articleTitle;

    private String articleTitleNew;
//    private String articleContent;

//    private String articleContentOrigin;

    private Long articleViews;

    private String articleViewsNew;

    private Long articleCommentCount;

    private Date articleDate;

    private Long articleLikeCount;

    private String articleCommentCountNew;

    private String articleLikeCountNew;

    private Integer articleType;

    private Integer articleUp;

    private Integer articleSupport;

    private Integer isDelete;

    private String articleIntroduce;

    private String articleIntroduceNew;

    private Long sortId;

    private String sortName;

    private String sortNameNew;

    private Long userId;

    private String userNickname;

    private String userNicknameNew;

    //存放label标签,例如['vue','css','jquery']
    private String labelsEntities;

    private Integer isTitleHigh = 0;

    private Integer isIntroduceHigh = 0;

    private Integer isNameHigh = 0;

}
