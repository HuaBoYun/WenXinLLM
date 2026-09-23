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
@TableName("Tbl_TASKFIND_ATT")
@Schema(name="TblTaskFindAtt", description="问题发现附件表")
public class TblTaskFindAtt implements Serializable {



    private static final long serialVersionUID = 1L;

    private BigDecimal attid;

    private BigDecimal findid;

    public BigDecimal getAttid() {
        return attid;
    }

    public void setAttid(BigDecimal attid) {
        this.attid = attid;
    }

	public BigDecimal getFindid() {
		return findid;
	}

	public void setFindid(BigDecimal findid) {
		this.findid = findid;
	}
  
}
