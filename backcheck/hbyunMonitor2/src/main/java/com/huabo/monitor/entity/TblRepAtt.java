package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-19
 */
@TableName("TBL_REP_ATT")
@Schema(name="TblRepAtt对象")
public class TblRepAtt implements Serializable {



    private static final long serialVersionUID = 1L;

    private BigDecimal attid;

    private BigDecimal reportid;
    
    private String isdecision ; //1为决策文件  0 | null 为非决策文件
    
    

    

	public String getIsdecision() {
		return isdecision;
	}

	public void setIsdecision(String isdecision) {
		this.isdecision = isdecision;
	}

	public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    public BigDecimal getReportid() {
        return reportid;
    }

    public void setReportid(BigDecimal reportid) {
        this.reportid = reportid;
    }

    @Override
    public String toString() {
        return "TblRepAtt{" +
            "attid=" + attid +
            ", reportid=" + reportid +
        "}";
    }
}
