package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@TableName("TBL_REPORTTEMPLE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class AgentEntity {
	
	@TableId(value = "reviewer")
	@Schema(name = "底稿复核人--一级")
	private BigDecimal reviewer;
	
	@TableField(value = "reviewer_2")
	@Schema(name = "底稿复核人--二级")
	private String reviewer_2;
	
	@TableField(value = "projectPerson")
	@Schema(name = "项目负责人复核人")
	private String projectPerson;
	
	@TableField(value = "responsible")
	@Schema(name = "事实确认人")
	private String responsible;
	
	@TableField(value = "takeAdvice")
	@Schema(name = "征求意见人")
	private String takeAdvice;
	
}
