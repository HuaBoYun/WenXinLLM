package com.huabo.audit.oracle.dto;

import java.math.BigDecimal;

import org.springframework.data.annotation.Transient;

import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计发现保存对象")
public class TblNbsjQuestionDto {
	
	private TblNbsjQuestionEntity tblNbsjQuestionEntity;
	
	@Schema(name = "ID")
	@Transient
	private Integer questionId;
	
	@Schema(name = "审计项目ID")
	@Transient
	private String projectId;
	
	@Schema(name = "项目编号")
	@Transient
	private String projectCode;
	
	@Schema(name = "项目名称")
	@Transient
	private String projectName;
	
	@Schema(name = "计划年份")
	@Transient
	private String planYear;
	
	@Schema(name = "问题标题")
	@Transient
	private String quesTitle;
	
	@Schema(name = "问题描述")
	@Transient
	private String auditDesc;
	
	@Schema(name = "审计单位")
	@Transient
	private String auditUnit;
	
	@Schema(name = "被审计单位")
	@Transient
	private String auditedUnit;
	
	@Schema(name = "发现人")
	@Transient
	private String findPeople;
	
	@Schema
	@Transient
	private Integer status;
	
	
	
}
