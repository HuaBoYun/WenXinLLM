package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-08-12
 */
@Data
@TableName("TBL_BUGCRITERION")
public class Criterion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
	@Schema
    private BigDecimal bugcriid;

	@Schema(name="缺陷级别")
    private String bugcrilevel;

	@Schema(name="定义")
    private String bugcridefine;

	@Schema(name="定量标准")
    private String bugcriration;

	@Schema(name="定性标准")
    private String bugcristability;

	@Schema
    private BigDecimal status;

	@Schema(name="版本")
    private BigDecimal version;

	@Schema
    private BigDecimal orgid;
}
