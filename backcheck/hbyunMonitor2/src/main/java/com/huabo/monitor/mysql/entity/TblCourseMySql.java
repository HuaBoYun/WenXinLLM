package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 视频学习  课程维护
 *
 * @author Lenovo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COURSE")
@Schema(name="TBL_COURSE")
public class TblCourseMySql {

    @TableId("COURSEID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal courseid;//课程id
    @TableField("COURSENAME")
    private String coursename;//课程名称
    @TableField("MEMO")
    private String memo;//课程简介
    @TableField("COURSENUMBER")
    private Integer coursenumber;//排课计划
    @TableField("PARENTID")
    private BigDecimal parentid;//上级id
    @TableField("PICURL")
    private String picurl;//海报路径
    @TableField("VIDEORUL")
    private String videourl;//视频路径
    @TableField("USERID")
    private BigDecimal userid;
    @TableField("ORGID")
    private BigDecimal orgid;
    @TableField("CREATEDATE")
    private Date createDate;//创建时间
    @TableField("COURSETYPE")
    private String coursetype;//所属系列分类
    @TableField("TYPE")
    private String type;//所属系列分类  1智能分析   2风险管控  3业务管控   4智能审计 5智能监控  6外部培训
    @TableField("COURSEWARE")
    private String courseware; //课件文档名称
    @TableField("COURSEWAREURL")
    private String coursewareurl; //课件文档url


}
