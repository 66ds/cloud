package com.qianbing.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2020-11-23 10:12:03
 */
@Data
@TableName("zj_comments")
public class CommentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long commentId;

	private Long userId;

	private Long articleId;

	private Long commentLikeCount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date commentDate;

	private String commentContent;

	private Long parentCommentId;

	private String commentSys;

	private String commentChrome;

	@TableField(exist = false)
	private List<CommentsEntity> children;

	@TableField(exist = false)
	private UsersEntity usersEntity;

	@TableField(exist = false)
	private UsersEntity parentUsersEntity;

	private Long isRead;
}
