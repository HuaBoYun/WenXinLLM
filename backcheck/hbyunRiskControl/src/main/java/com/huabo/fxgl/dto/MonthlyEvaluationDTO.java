package com.huabo.fxgl.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 月度评估情况一览表 DTO
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Data
public class MonthlyEvaluationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 风险编号
     */
    private String risknumber;

    /**
     * 所属公司
     */
    private String unitname;

    /**
     * 风险名称
     */
    private String riskname;

    /**
     * 创建时间
     */
    private String riskcreatedt;

    /**
     * 是否关闭 (0已关闭、1未关闭)
     */
    private Integer riskstatus;

    /**
     * 变化趋势 (1升高、2持平、3下降，其余未评估) - 字符串类型
     */
    private String riskchange;

    /**
     * 控制措施数
     */
    private Integer copingcount;
}

