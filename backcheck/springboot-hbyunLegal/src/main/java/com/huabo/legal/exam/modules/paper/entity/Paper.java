package com.huabo.legal.exam.modules.paper.entity;

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
* 试卷实体类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 17:31
*/
@Data
@TableName("tbl_fwgl_paper")
public class Paper extends Model<Paper> {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("userid")
    private String userId;

    /**
     * 部门ID
     */
    @TableField("departid")
    private String departId;

    /**
     * 规则ID
     */
    @TableField("examid")
    private String examId;

    /**
     * 考试标题
     */
    @TableField("title")
    private String title;

    /**
     * 考试时长
     */
    @TableField("totaltime")
    private Integer totalTime;

    /**
     * 用户时长
     */
    @TableField("usertime")
    private Integer userTime;

    /**
     * 试卷总分
     */
    @TableField("totalscore")
    private Integer totalScore;

    /**
     * 及格分
     */
    @TableField("qualifyscore")
    private Integer qualifyScore;

    /**
     * 客观分
     */
    @TableField("objscore")
    private Integer objScore;

    /**
     * 主观分
     */
    @TableField("subjscore")
    private Integer subjScore;

    /**
     * 用户得分
     */
    @TableField("userscore")
    private Integer userScore;

    /**
     * 是否包含简答题
     */
    @TableField("hassaq")
    private Boolean hasSaq;

    /**
     * 试卷状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("createtime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updatetime")
    private Date updateTime;

    /**
     * 截止时间
     */
    @TableField("limittime")
    private Date limitTime;
}
