package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("TBL_RISKEVENT_MATRIX")
public class RiskeventMatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema
    private BigDecimal riseveid;

	@Schema
    private BigDecimal conmatid;

	@Schema(name="风险控制点编号")
    @TableField(exist = false)
    private String controlnumber;
}
