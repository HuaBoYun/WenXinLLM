package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName(value = "TBL_RISK_ASSPLAN_RISK", resultMap = "RM_RISK_ASSPLAN_RISK")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class RiskAssplanRisk extends FlexibleFieldEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 风险ID
     */
    @Schema(name="风险ID")
    @TableField(value = "RISKID", property = "risk.riskid")
    private Risk risk;

	@Schema
    private BigDecimal riskid;

	@Schema(name="主键ID")
    @TableId(type = IdType.INPUT)
    private BigDecimal assriskid;


    /**
     * 评估计划ID
     */
	@Schema(name="评估计划ID")
    @TableField(value = "ASSPLANID", property = "assplan.assplanid")
    private RiskAssplan assplan;

	@Schema
    private BigDecimal assplanid;

    /**
     * ID
     */
	@Schema(name="ID")
    @TableField(exist = false)
    private RiskRiskmarking riskmarking;
    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

    /**
     * 状态
     */
	@Schema(name="状态")
    private String assstatus;

    /**
     * 风险等级
     */
	@Schema(name="风险等级")
    private String risklevel;

    /**
     * 发生频率
     */
	@Schema(name="发生频率")
    private BigDecimal frequency;

    /**
     * 严重程度
     */
	@Schema(name="严重程度")
    private BigDecimal severity;

    /**
     * 时间
     */
	@Schema(name="时间")
    private LocalDateTime assdate;

    @TableField(exist = false)
    private Set<RiskRiskmarking> tblRiskRiskMarking = new HashSet<RiskRiskmarking>();


    private String riskIds;
    
}
