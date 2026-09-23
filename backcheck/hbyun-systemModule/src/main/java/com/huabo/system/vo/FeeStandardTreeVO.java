package com.huabo.system.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FeeStandardTreeVO", description = "费用标准树形节点")
public class FeeStandardTreeVO {

    @Schema(name = "TBL_SYSTEM_RIGHT.ID")
    private BigDecimal id;

    @Schema(name = "菜单名称")
    private String name;

    @Schema(name = "上级ID")
    private BigDecimal parent;

    @Schema(name = "类型 0=目录 1=页面 2=操作")
    private Integer type;

    @Schema(name = "唯一标识")
    private String perms;

    @Schema(name = "路由地址")
    private String path;

    @Schema(name = "所属模块")
    private String moduletype;

    @Schema(name = "是否隐藏 1=不隐藏 0=隐藏")
    private Integer visible;

    @Schema(name = "费用标准ID")
    private BigDecimal feeStandardId;

    @Schema(name = "计费方式 1=按次 2=按调用量")
    private Integer feeType;

    @Schema(name = "计费金额")
    private BigDecimal feeAmount;

    @Schema(name = "费用标准状态 1=启用 0=禁用")
    private Integer feeStatus;

    @Schema(name = "最后修改时间")
    private Date updateTime;

    @Schema(name = "最后修改人")
    private String updateBy;

    @Schema(name = "阶梯规则（按调用量时使用）")
    private List<Object> tiers;

    @Schema(name = "子节点")
    private List<FeeStandardTreeVO> children;
}
