package com.huabo.legal.exam.modules.user.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
* <p>
* 错题本实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-27 17:56
*/
@Data
@TableName("tbl_fwgl_user_book")
public class UserBook extends Model<UserBook> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 考试ID
     */
    @TableField("examid")
    private String examId;

    /**
     * 用户ID
     */
    @TableField("userid")
    private String userId;

    /**
     * 题目ID
     */
    @TableField("quid")
    private String quId;

    /**
     * 加入时间
     */
    @TableField("createtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最近错误时间
     */
    @TableField("updatetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 错误时间
     */
    @TableField("wrongcount")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Integer wrongCount;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 错题序号
     */
    private Integer sort;
    
}
