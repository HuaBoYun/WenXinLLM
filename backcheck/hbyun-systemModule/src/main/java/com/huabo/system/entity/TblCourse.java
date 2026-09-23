package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 视频学习  课程维护
 * @author Lenovo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COURSE")
@Schema(name="TBL_COURSE", description="")
public class TblCourse {

	@TableId(value="COURSEID",type = IdType.INPUT)
	@Schema(name= "主键")
	private BigDecimal courseid;//课程id
	@TableField("COURSENAME")
	@Schema(name= "课程名称")
	private String coursename;//课程名称
	@TableField("MEMO")
	@Schema(name= "课程简介")
	private String memo;//课程简介
	@TableField("COURSENUMBER")
	@Schema(name= "排课计划")
	private Integer coursenumber;//排课计划
	@TableField("PARENTID")
	@Schema(name= "上级ID")
	private	BigDecimal parentid;//上级id
	@TableField("PICURL")
	@Schema(name= "海报路径")
	private String picurl;//海报路径
	@TableField("VIDEORUL")
	@Schema(name= "视频路径")
	private String videourl;//视频路径
	@TableField("USERID")
	@Schema(name= "创建人ID")
	private BigDecimal userid;
	@TableField("ORGID")
	@Schema(name= "隶属组织ID")
	private BigDecimal orgid;
	@TableField("CREATEDATE")
	@Schema(name= "创建时间")
	private Date createDate;//创建时间
	@TableField("COURSETYPE")
	@Schema(name= "所属系列分类")
	private String coursetype;//所属系列分类
	@TableField("TYPE")
	@Schema(name= "所属模块")
	private String type;//所属系列分类  1智能分析   2风险管控  3业务管控   4智能审计 5智能监控  6外部培训
	@TableField("COURSEWARE")
	@Schema(name= "课件文档名称")
	private String courseware; //课件文档名称
	@TableField("COURSEWAREURL")
	@Schema(name= "课件文档地址")
	private String coursewareurl; //课件文档url

	
	
}
