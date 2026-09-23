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
 * 
 */
@TableName("TBL_ASSESS_ATT")
@Schema(name="TblAssessAtt对象")
public class TblAssessAtt implements Serializable {



    private static final long serialVersionUID = 1L;

    private BigDecimal attid;

    private BigDecimal assid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }
    

    public BigDecimal getAssid() {
		return assid;
	}

	public void setAssid(BigDecimal assid) {
		this.assid = assid;
	}
 
}
