package com.huabo.fxgl.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("TBL_RISK_IMPROVEMENT_DETAILS")
@Schema(name="风险报送-风险监督改进详情列表")
public class TblRiskImprovementDetailsEntiry  implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "ID")
    private BigDecimal id;

    @TableField(value = "BRANCH_NAME")
    @Schema(name="分公司名称")
    private String branchName;
    
    @TableField(value = "BRANCH_ID")
    @Schema(name="分公司ID")
    private BigDecimal branchId;

    @TableField(value = "YEARS")
    @Schema(name="年份")
    private BigDecimal years;
    @TableField(value = "MONTH")
    @Schema(name="月份")
    private BigDecimal month;
    @TableField(value = "IS_REPORT")
    @Schema(name="是否上报")
    private String isReport;
    @TableField(value = "SCORE_DETAILS")
    @Schema(name="扣分情况")
    private String scoreDetails;

    @TableField(value = "IMPROVEMENT_ID")
    @Schema(name="下发表单id")
    private BigDecimal improvementId;

    
    @Schema(name="风险名称")
    @TableField(exist=false,value = "IMPROVEMENT_ID")
    private String impRiskName;
    
    @TableField(exist=false,value = "IMP_RISK_NAME")
    @Schema(name="风险描述") 
    private String impRiskDetails;

    @TableField(value = "RISKID")
    @Schema(name="风险点id")
    private BigDecimal riskid;
    
    @TableField(value = "CREATTIME")
    @Schema(name="创建时间")
    private Date creatTime;
    
    @TableField(value = "RISKNUMBER",exist=false)
    @Schema(name="风险点编号")
    private String risknumber;
    
    
    
    @TableField(exist=false,value = "ORGNAME")
    @Schema(name="责任部门") 
    private String orgName;


}
