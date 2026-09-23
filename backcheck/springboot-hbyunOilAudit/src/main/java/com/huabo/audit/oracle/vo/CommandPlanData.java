package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommandPlanData {
	 
	@Schema(name = "查询年度")
	private Integer queryYear;
	
	@Schema(name = "年初计划执行率")
	private BigDecimal yearBeginPlanExcuteRate;
	
	@Schema(name = "已安排计划数")
	private Integer yearBeginPlanExcuteNum;
	
	@Schema(name = "年初计划数")
	private Integer yearBeginPlanNum;
	
	@Schema(name = "审计发现问题数量")
	private Integer auditFindQuesNum;
	
	@Schema(name = "审计发现问题金额")
	private BigDecimal auditFindMoneyNum;
	
	@Schema(name = "审计发现问题整改率")
	private BigDecimal auditFindQuesRate;
	
	@Schema(name = "审计发现问题金额整改率")
	private BigDecimal auditFindMoneRate;
	
	@Schema(name = "审计整改问题数量")
	private Integer auditRectQuesNum;
	
	@Schema(name = "审计整改问题金额")
	private BigDecimal auditQuesMoneyNum;
	
	@Schema(name = "审计已建议采纳率")
	private BigDecimal auditadoptionRate;
	
	@Schema(name = "审计建议采纳数")
	private Integer auditadoptionNum;
	
	@Schema(name = "提出审计建议数")
	private Integer auditadoptionTotalNum;
	
	//--项目运行情况
	@Schema(name = "审前准备数")
	private Integer sqzbNum;
	
	@Schema(name = "现场实施数")
	private Integer xcssNum;
	
	@Schema(name = "审计报告数")
	private Integer sjbgNum;
	
	@Schema(name = "审计整改数")
	private Integer sjzgNum;
	
	@Schema(name = "已完成数")
	private Integer ywcNum;
	
	//--人员情况
	@Schema(name = "请假人员数")
	private Integer staffQjNum;
	
	@Schema(name = "在岗闲置人员数")
	private Integer staffZgxzNum;
	
	@Schema(name = "在岗项目内人员数")
	private Integer staffZgxmnNum;
	
	@Schema(name = "外派人员数")
	private Integer staffWpNum;
	
}