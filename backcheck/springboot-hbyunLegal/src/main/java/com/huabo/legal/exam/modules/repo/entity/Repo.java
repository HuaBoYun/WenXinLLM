package com.huabo.legal.exam.modules.repo.entity;

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
* 题库实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@TableName("tbl_fwgl_repo")
public class Repo extends Model<Repo> {

    private static final long serialVersionUID = 1L;

    /**
     * 题库ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 题库编号
     */
    @TableField("CODE")
    private String code;

    /**
     * 题库名称
     */
    @TableField("TITLE")
    private String title;

    /**
     * 单选数量
     */
    @TableField("radiocount")
    private Integer radioCount;

    /**
     * 多选数量
     */
    @TableField("multicount")
    private Integer multiCount;

    /**
     * 判断数量
     */
    @TableField("judgecount")
    private Integer judgeCount;


    /**
     * 题库备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("createtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updatetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
}
