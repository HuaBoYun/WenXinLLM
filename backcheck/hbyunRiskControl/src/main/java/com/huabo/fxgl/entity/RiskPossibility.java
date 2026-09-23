package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
@ToString
@TableName("TBL_RISK_POSSIBILITY")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
//风险发生频率
public class RiskPossibility implements Serializable {

    private static final long serialVersionUID = 1L;

    //[{"possid": "","rplevel": "1","possdes": "说明1"},{"possid": "","rplevel": "2","possdes": "说明2"},{"possid": "","rplevel": "3","possdes": "说明3"},{"possid": "","rplevel": "4","possdes": "说明4"},{"possid": "","rplevel": "5","possdes": "说明5"}]
    @TableId(type = IdType.INPUT)
	@Schema(name="主键ID")
    private BigDecimal possid;

	@Schema(name="等级")
    private BigDecimal rplevel;

	@Schema(name="说明")
    private String possdes;

	@Schema
    private String memo;

	@Schema(name="关联风险评估标准id")
    private BigDecimal assstdid;

	public RiskPossibility(RiskPossibility r) {
		super();
		this.rplevel = r.rplevel;
		this.possdes = r.possdes;
		this.memo = r.memo;
		this.assstdid = r.assstdid;
	}

	public RiskPossibility() {
	}


}
