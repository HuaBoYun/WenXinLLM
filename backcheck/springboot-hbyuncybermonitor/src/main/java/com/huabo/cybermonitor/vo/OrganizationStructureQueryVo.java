package com.huabo.cybermonitor.vo;

import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织架构管理查询VO
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="OrganizationStructureQueryVo", description="组织架构管理查询VO")
public class OrganizationStructureQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "父级组织ID")
    private String parentOrgId;

    @Schema(name = "组织编码")
    private String orgCode;

    @Schema(name = "组织名称")
    private String orgName;

    @Schema(name = "组织类型")
    private String orgType;

    @Schema(name = "组织级别")
    private Integer orgLevel;

    @Schema(name = "组织状态")
    private String orgStatus;

    @Schema(name = "负责人")
    private String leader;

    @Schema(name = "负责人职务")
    private String leaderPosition;

    @Schema(name = "审批状态")
    private String approvalStatus;

    @Schema(name = "是否叶子节点")
    private String isLeaf;

    @Schema(name = "成立时间-开始")
    private LocalDateTime establishmentTimeBegin;

    @Schema(name = "成立时间-结束")
    private LocalDateTime establishmentTimeEnd;

    @Schema(name = "审批时间-开始")
    private LocalDateTime approvalTimeBegin;

    @Schema(name = "审批时间-结束")
    private LocalDateTime approvalTimeEnd;

    @Schema(name = "人员编制-最小值")
    private Integer staffEstablishmentMin;

    @Schema(name = "人员编制-最大值")
    private Integer staffEstablishmentMax;

    @Schema(name = "实际人数-最小值")
    private Integer actualStaffCountMin;

    @Schema(name = "实际人数-最大值")
    private Integer actualStaffCountMax;

    @Schema(name = "管理幅度-最小值")
    private Integer managementSpanMin;

    @Schema(name = "管理幅度-最大值")
    private Integer managementSpanMax;
}
