package com.qianbing.common.vo;

import lombok.Data;

@Data
public class DigVo extends CommonVo {

    private Long articleId;

    private String content;

    private Long alias;//1表示文章 2表示评论(删除标识)
}
