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
 * @date 2022-03-09 13:26:32
 */
@Data
@TableName("zj_music")
public class MusicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Integer zjMusicId;
	/**
	 * $column.comments
	 */
	private String zjMusicName;
	/**
	 * $column.comments
	 */
	private String zjMusicCover;
	/**
	 * $column.comments
	 */
	private String zjMusicUrl;
	/**
	 * $column.comments
	 */
	private String zjMusicArtist;
	/**
	 * $column.comments
	 */
	private String zjMusicLrc;
	/**
	 * $column.comments
	 */
	private Integer zjMusicIsPause;
	/**
	 * $column.comments
	 */
	private Integer zjMusicIsDelete;
	/**
	 * $column.comments
	 */
	private String zjMusicPlayTime;
	/**
	 * $column.comments
	 */
	private Date zjMusicCreateTime;
	/**
	 * $column.comments
	 */
	private Date zjMusicUpdateTime;
	/**
	 * $column.comments
	 */
	private Integer zjMusicIsCollect;
	/**
	 * $column.comments
	 */
	private Integer zjMusicRecentPlay;

}
