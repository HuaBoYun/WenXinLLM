package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 费用标准变更日志表（价格快照）
 * 记录每次费用标准变更的历史快照
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_STANDARD_LOG")
@Schema(name="TblFeeStandardLog对象", description="费用标准变更日志表")
public class TblFeeStandardLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键（价格快照ID）")
    @TableId(value="ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name="关联TBL_SYSTEM_RIGHT.ID")
    @TableField("RIGHT_ID")
    private BigDecimal rightId;

    @Schema(name="菜单名称快照")
    @TableField("RIGHT_NAME")
    private String rightName;

    @Schema(name="所属大模块快照")
    @TableField("MODULE_TYPE")
    private String moduleType;

    @Schema(name="计费方式快照 1=按次 2=按调用量")
    @TableField("FEE_TYPE")
    private Integer feeType;

    @Schema(name="计费金额快照")
    @TableField("FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Schema(name="变更时间")
    @TableField("CHANGE_TIME")
    private Date changeTime;

    @Schema(name="变更人")
    @TableField("CHANGE_BY")
    private String changeBy;

    @Schema(name="变更备注")
    @TableField("REMARK")
    private String remark;
}
