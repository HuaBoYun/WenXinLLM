package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Data
@TableName("TBL_RISK_OUTERRULE")
public class RiskOuterrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="风险主键")
    private BigDecimal riskid;

    @Schema(name="外规主键")
    private BigDecimal outrulid;


    @Override
    public String toString() {
        return "RiskOuterrule{" +
            "riskid=" + riskid +
            ", outrulid=" + outrulid +
        "}";
    }
}
