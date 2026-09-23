package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("TBL_COM_EXT_TESTTASK_ATT")
@Schema(name="TblTesttaskAtt对象")
public class TblTesttaskAtt implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal attid;

    private BigDecimal testtaskid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getTesttaskid() {
        return testtaskid;
    }

    public void setTesttaskid(BigDecimal testtaskid) {
        this.testtaskid = testtaskid;
    }

    @Override
    public String toString() {
        return "TblTesttaskAtt{" +
            "attid=" + attid +
            ", testtaskid=" + testtaskid +
        "}";
    }
}
