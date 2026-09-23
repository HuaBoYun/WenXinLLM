package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("TBL_RISK_MONTHLY_EVALUATION")
@Schema(name="风险报送-分公司上报-单位月度风险评估表")
public class TblRiskMonthlyEvaluationEntity extends FlexibleFieldEntity  implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "ID")
    private BigDecimal id;
    @TableField(value = "UNIT_DEPTID")
    @Schema(name="责任单位（部门）")
    private BigDecimal unitDeptid;
    @TableField(exist = false)
    @Schema(name="责任单位（部门））名称")
    private String unitDeptidName;
    @TableField(value = "ONE_RISK")
    @Schema(name="一级风险")
    private String oneRisk;
    @TableField(value = "TWO_RISK")
    @Schema(name="二级风险")
    private String twoRisk;
    @TableField(value = "THREE_RISK")
    @Schema(name="三级风险")
    private String threeRisk;
    @TableField(value = "DETAIL_RISK")
    @Schema(name="风险描述")
    private String detailRisk;
    @TableField(value = "ANALYSIS_RISK")
    @Schema(name="风险源分析（导致风险发生的潜在因素）")
    private String analysisRisk;
    @TableField(value = "ANALYSIS_SOL")
    @Schema(name="现有应当措施")
    private String analysisSol;
    @TableField(value = "ANALYSIS_REL")
    @Schema(name="相关制度和规程索引")
    private String analysisRel;
    @TableField(value = "ANALYSIS_EVE")
    @Schema(name="典型风险事件描述")
    private String analysisEve;
    @TableField(value = "ASSESS_STAN")
    @Schema(name="风险可能性评价标准")
    private String assessStan;
    @TableField(value = "ASSESS_SCORE_ONE")
    @Schema(name="对应分值")
    private String assessScoreOne;
    @TableField(value = "ASSESS_INF")
    @Schema(name="风险影响程度评价标准")
    private String assessInf;
    @TableField(value = "ASSESS_SCORE_TWO")
    @Schema(name="对应分值")
    private String assessScoreTwo;
    @TableField(value = "ASSESS_SCO")
    @Schema(name="风险评分")
    private String assessSco;
    @TableField(value = "ASSESS_GRADE")
    @Schema(name="风险等级")
    private String assessGrade;
    @TableField(value = "MONTH_MEA")
    @Schema(name="本月风险管控措施及实施情况")
    private String monthMea;
    @TableField(value = "RISK_CHANGE")
    @Schema(name="风险变化趋势（1:升高/2:持平/3:下降）")
    private String riskChange;
    @TableField(value = "NEXT_MONTH_MEA")
    @Schema(name="下月风险管控措施")
    private String nextMonthMea;
    @TableField(value = "IS_NEW_RISK")
    @Schema(name="是否新增风险/1:已有风险/0:关闭风险")
    private BigDecimal isNewRisk;
    @TableField(value = "CREATE_STAFFID")
    @Schema(name="创建人")
    private BigDecimal createStaffid;
    @TableField(exist = false)
    @Schema(name="创建人名称")
    private String createStaffidName;
    @TableField(value = "CREATE_TIME")
    @Schema(name="创建时间")
    private Date createTime;
    @TableField(value = "CREATE_DEPTID")
    @Schema(name="创建部门")
    private BigDecimal createDeptid;
    @TableField(exist = false)
    @Schema(name="创建部门名称")
    private String createDeptidName;
    @TableField(value = "CREATE_UNITID")
    @Schema(name="创建单位")
    private BigDecimal createUnitid;
    @TableField(exist = false)
    @Schema(name="创建单位名称")
    private String createUnitidName;

    @TableField(value = "IMPLEMENTID")
    @Schema(name="下发表单ID")
    private BigDecimal implementId;

    @TableField(value = "MONTH")
    @Schema(name="月份")
    private BigDecimal month;

    @TableField(value = "REPORT_BUTTON_STATUS")
    @Schema(name="上报按钮,0：未上报,1：已上报")
    private BigDecimal reportButtonStatus;

    @TableField(value = "RISKID")
    @Schema(name="关联风险点id")
    private BigDecimal riskid;

    @TableField(value = "RISKNAME",exist=false)
    @Schema(name="关联风险点名称")
    private String risknumber;
    
    @Schema(name="上报审批状态")
    private BigDecimal status;

    @Schema(name="公司责任领导")
    @TableField("LEADERSHIP")
    @Column(name = "LEADERSHIP")
    private String leadership;
 
	@Schema(name="配合单位或部门")
    @TableField("COOPERATEORG")
    @Column(name = "COOPERATEORG")
    private String cooperateOrg;
	
	
	@Schema(name="公司责任领导NAME")
    @TableField(exist=false)
    private String leadershipName;
 
	@Schema(name="配合单位或部门NAME")
    @TableField("COOPERATEORGNAME")
    private String cooperateOrgName;
	@Schema(name="四级风险")
	@TableField("LEVELFOURRISK")
    private String LevelFourRisk;
//
	   @TableField(exist=false)
	    @Schema(name="查询条件上报月份L")
	    private BigDecimal monthL;
	   @TableField(exist=false)
	    @Schema(name="查询条件上报月份R")
	    private BigDecimal monthR;
	   
	   
		@Schema(name="上升/下降原因")
	    @TableField("REASON")
	    @Column(name = "REASON")
	    private String reason;
		
		@Schema(name="管控措施是否逾期")
	    @TableField("ISOVERDUE")
	    @Column(name = "ISOVERDUE")
	    private String isoverdue;
	   
		    @TableField(exist = false)
		    @Schema(name="查询条件年度")
		    private String year;
		    
		    
	
		  //密级及查询条件
		    @Schema(name = "密级主键")
		    @TableField("SECRECTLEVELID")
		    private BigDecimal secrectLevelId;
		    
		    @Schema(name = "知悉范围id")
		    @TableField("STAFFSCOPEIDS")
		    private String staffScopeIds;
		    
		    @Schema(name = "知悉范围名称")
		    @TableField("STAFFSCOPENAMES")
		    private String staffScopeNames;
	   
}
