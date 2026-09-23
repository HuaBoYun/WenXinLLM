package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
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
@ToString
@TableName("TBL_RISK_COPING_CMATRIX")
public class RiskCopingCmatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "控制措施id")
    private BigDecimal conmatid;

    @Column(name = "风险应对id")
    private BigDecimal riskcopingid;

    public RiskCopingCmatrix(BigDecimal conmatid, BigDecimal riskcopingid) {
        this.conmatid = conmatid;
        this.riskcopingid = riskcopingid;
    }
}
