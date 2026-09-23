package com.huabo.system.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阶梯计费规则表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_STANDARD_TIER")
@Schema(name = "TblFeeStandardTier对象", description = "阶梯计费规则表")
public class TblFeeStandardTier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name = "关联TBL_FEE_STANDARD.ID")
    @TableField("STANDARD_ID")
    private BigDecimal standardId;

    @Schema(name = "阶梯起始调用量（含）")
    @TableField("MIN_COUNT")
    private Integer minCount;

    @Schema(name = "阶梯结束调用量（含），NULL表示无上限")
    @TableField("MAX_COUNT")
    private Integer maxCount;

    @Schema(name = "该阶梯的单价（元/次）")
    @TableField("FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Schema(name = "排序")
    @TableField("SORT")
    private Integer sort;
}
