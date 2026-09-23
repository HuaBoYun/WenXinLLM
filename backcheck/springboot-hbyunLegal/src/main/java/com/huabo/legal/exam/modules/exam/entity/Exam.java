package com.huabo.legal.exam.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import java.util.Date;

import javax.persistence.Column;

/**
* <p>
* 考试实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Data
@TableName("tbl_fwgl_exam")
public class Exam extends Model<Exam> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 考试名称
     */
    private String title;

    /**
     * 考试描述
     */
    private String content;

    /**
     * 1公开2部门3定员
     */
    @TableField("opentype")
    private Integer openType;

    /**
     * 组题方式1题库,2指定
     */
    @TableField("joinType")
    private Integer joinType;

    /**
     * 难度:0不限,1普通,2较难
     */
	@TableField("examLevel")
    private Integer examLevel;

    /**
     * 考试状态 0-进行中 1-已禁用 2-待开始 3-已结束
     */
    private Integer state;

    /**
     * 是否限时
     */
    @TableField("timeLimit")
    private Boolean timeLimit;

    /**
     * 开始时间
     */
    @TableField("startTime")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("endTime")
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 总分数
     */
    @TableField("totalScore")
    private Integer totalScore;

    /**
     * 总时长（分钟）
     */
    @TableField("totalTime")
    private Integer totalTime;

    /**
     * 及格分数
     */
    @TableField("qualifyScore")
    private Integer qualifyScore;
    
}
