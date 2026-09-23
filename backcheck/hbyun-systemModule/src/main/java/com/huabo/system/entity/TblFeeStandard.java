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
 * 费用标准配置表
 * 存储各菜单节点的计费方式和计费金额
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FEE_STANDARD")
@Schema(name="TblFeeStandard对象", description="费用标准配置表")
public class TblFeeStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键")
    @TableId(value="ID", type = IdType.INPUT)
    private BigDecimal id;

    @Schema(name="关联TBL_SYSTEM_RIGHT.ID")
    @TableField("RIGHT_ID")
    private BigDecimal rightId;

    @Schema(name="菜单名称")
    @TableField("RIGHT_NAME")
    private String rightName;

    @Schema(name="所属大模块")
    @TableField("MODULE_TYPE")
    private String moduleType;

    @Schema(name="计费方式 1=按次 2=按调用量")
    @TableField("FEE_TYPE")
    private Integer feeType;

    @Schema(name="计费金额")
    @TableField("FEE_AMOUNT")
    private BigDecimal feeAmount;

    @Schema(name="状态 1=启用 0=禁用")
    @TableField("STATUS")
    private Integer status;

    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(name="修改人")
    @TableField("UPDATE_BY")
    private String updateBy;
}
