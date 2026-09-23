package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * @since 2022-08-05
 */
@Data
@ToString
@Schema(name="TBL_RISK_CONTROLMATRIX", description="控制措施和风险中间表")
@TableName("TBL_RISK_CONTROLMATRIX")
public class RiskControlmatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="控制措施")
    @TableField("CONMATID")
    private BigDecimal conmatid;

	@Schema(name="主键ID")
	 @TableField("RISKID")
    private BigDecimal riskid;

}
