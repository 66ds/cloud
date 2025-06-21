package com.qianbing.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author qianbing
 * @email 1532498760@qq.com
 * @date 2022-03-11 15:42:05
 */
@Data
@TableName("zj_mv")
public class MvEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer zjMvId;
	/**
	 * $column.comments
	 */
	private String zjMvUrl;
	/**
	 * $column.comments
	 */
	private String zjMvPhoto;
	/**
	 * $column.comments
	 */
	private Integer zjMvIsPause;
	/**
	 * $column.comments
	 */
	private Date zjMvCreateTime;
	/**
	 * $column.comments
	 */
	private Date zjMvUpdateTime;

	private String zjMvName;

}
