package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
@TableName("TBL_TESTPLAN_ATT")
@Schema(name="TblTestplanAtt对象")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblTestplanAtt implements Serializable {

    private static final long serialVersionUID = 1L;
    // 联合主键
    private BigDecimal attid;
    private BigDecimal testplanid;




    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getTestplanid() {
        return testplanid;
    }

    public void setTestplanid(BigDecimal testplanid) {
        this.testplanid = testplanid;
    }

    @Override
    public String toString() {
        return "TblTestplanAtt{" +
            "attid=" + attid +
            ", testplanid=" + testplanid +
        "}";
    }
}
