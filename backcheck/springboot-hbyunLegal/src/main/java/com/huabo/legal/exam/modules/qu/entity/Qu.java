package com.huabo.legal.exam.modules.qu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

/**
* <p>
* 问题题目实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@TableName("tbl_fwgl_qu")
public class Qu extends Model<Qu> {

    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 题目类型
     */
    @TableField("qutype")
    private Integer quType;

    /**
     * 1普通,2较难
     */
    private Integer levela;

    /**
     * 题目图片
     */
    private String image;

    /**
     * 题目内容
     */
    private String content;
    
    @Schema(name = "所属题库名称")
    @Transient
    private String title;
    
    @Schema(name = "所属题库id")
    @Transient
    private String tblrepoid;

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

    /**
     * 题目备注
     */
    private String remark;

    /**
     * 整题解析
     */
    private String analysis;
    
}
