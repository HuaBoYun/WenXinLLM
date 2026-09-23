package com.huabo.fxgl.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("TBL_RISK_IMPROVEMENT")
@Schema(name="风险报送-风险监督改进")
public class TblRiskImprovementEntiry  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    private BigDecimal id;
    @TableField(value = "BRANCH_ID")
    @Schema(name="分公司id")
    private BigDecimal branchId;
    @TableField(value = "BRANCH_NAME")
    @Schema(name="分公司名称")
    private String branchName;
    @TableField(value = "SCORE")
    @Schema(name="总数")
    private BigDecimal score;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private String status;
    
    @TableField(value = "MONTH")
    @Schema(name="月份")
    private BigDecimal month;
    
    @TableField(value = "YEAR")
    @Schema(name="月份")
    private BigDecimal year;

    @TableField(value = "RISK_IMPLEMENT_ID")
    @Schema(name="下发表单ID")
    private BigDecimal riskImplementID;
}
