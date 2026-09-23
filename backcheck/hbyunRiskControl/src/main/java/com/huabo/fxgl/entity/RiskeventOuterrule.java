package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
@TableName("TBL_RISKEVENT_OUTERRULE")
public class RiskeventOuterrule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema
    private BigDecimal riseveid;

	@Schema
    private BigDecimal outrulid;

	@Schema
    @TableField(exist = false)
    private String outrulName;

}
