package com.qianbing.comment.vo;


import lombok.Data;


@Data
public class CommentVo extends CommonVo {

    private Long articleId;

    private String articleTitle;

}
