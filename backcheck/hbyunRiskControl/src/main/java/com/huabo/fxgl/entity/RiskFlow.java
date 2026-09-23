package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("TBL_RISK_FLOW")
public class RiskFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal riskid;

    private BigDecimal flowid;

}
