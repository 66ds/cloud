package com.qianbing.article.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticlesVo {

    private Long articleId;

    private String articleTitle;

    private String articleContent;

    private String articleContentOrigin;

    private Long articleViews;

    private Long articleCommentCount;

    private Date articleDate;

    private Long articleLikeCount;

    private Integer articleType;

    private Integer articleUp;

    private Integer articleSupport;

    private Integer isDelete;

    private String articleIntroduce;

    private List<Long> sortIds;

    private String sortName;

    private List<String> labelNames;

    private Long userId;

    private String userNickname;
}
