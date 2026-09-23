package com.financial.sharing.groupControl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 维度成员查询参数
 * 
 * @author system
 * @since 2026-01-30
 */
@Data
@Schema(description = "维度成员查询参数")
public class DimensionMemberQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维度ID
     */
    @Schema(description = "维度ID")
    private String dimensionId;

    /**
     * 成员ID
     */
    @Schema(description = "成员ID")
    private String memberId;

    /**
     * 父成员ID
     */
    @Schema(description = "父成员ID")
    private String parentMemberId;

    /**
     * 成员编码
     */
    @Schema(description = "成员编码")
    private String memberCode;

    /**
     * 成员名称
     */
    @Schema(description = "成员名称")
    private String memberName;

    /**
     * 成员类型
     */
    @Schema(description = "成员类型")
    private String memberType;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private String tenantId;
}

