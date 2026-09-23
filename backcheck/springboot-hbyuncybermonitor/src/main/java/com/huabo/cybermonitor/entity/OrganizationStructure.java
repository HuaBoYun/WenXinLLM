package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 组织架构管理实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ORGANIZATION_STRUCTURE")
@Schema(name="OrganizationStructure", description="组织架构管理实体")
public class OrganizationStructure {

    @Schema(name = "组织架构ID")
    @TableId(value = "ORG_ID", type = IdType.ASSIGN_UUID)
    private String orgId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "父级组织ID")
    @TableField("PARENT_ORG_ID")
    private String parentOrgId;

    @Schema(name = "组织编码")
    @TableField("ORG_CODE")
    private String orgCode;

    @Schema(name = "组织名称")
    @TableField("ORG_NAME")
    private String orgName;

    @Schema(name = "组织简称")
    @TableField("ORG_SHORT_NAME")
    private String orgShortName;

    @Schema(name = "组织类型")
    @TableField("ORG_TYPE")
    private String orgType;

    @Schema(name = "组织级别")
    @TableField("ORG_LEVEL")
    private Integer orgLevel;

    @Schema(name = "组织层级路径")
    @TableField("ORG_PATH")
    private String orgPath;

    @Schema(name = "排序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name = "组织状态")
    @TableField("ORG_STATUS")
    private String orgStatus;

    @Schema(name = "成立时间")
    @TableField("ESTABLISHMENT_TIME")
    private LocalDateTime establishmentTime;

    @Schema(name = "撤销时间")
    @TableField("DISSOLUTION_TIME")
    private LocalDateTime dissolutionTime;

    @Schema(name = "负责人")
    @TableField("LEADER")
    private String leader;

    @Schema(name = "负责人职务")
    @TableField("LEADER_POSITION")
    private String leaderPosition;

    @Schema(name = "联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @Schema(name = "联系邮箱")
    @TableField("CONTACT_EMAIL")
    private String contactEmail;

    @Schema(name = "办公地址")
    @TableField("OFFICE_ADDRESS")
    private String officeAddress;

    @Schema(name = "职能描述")
    @TableField("FUNCTION_DESCRIPTION")
    private String functionDescription;

    @Schema(name = "主要职责")
    @TableField("MAIN_RESPONSIBILITIES")
    private String mainResponsibilities;

    @Schema(name = "业务范围")
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    @Schema(name = "管理权限")
    @TableField("MANAGEMENT_AUTHORITY")
    private String managementAuthority;

    @Schema(name = "人员编制")
    @TableField("STAFF_ESTABLISHMENT")
    private Integer staffEstablishment;

    @Schema(name = "实际人数")
    @TableField("ACTUAL_STAFF_COUNT")
    private Integer actualStaffCount;

    @Schema(name = "预算额度")
    @TableField("BUDGET_AMOUNT")
    private String budgetAmount;

    @Schema(name = "成本中心")
    @TableField("COST_CENTER")
    private String costCenter;

    @Schema(name = "利润中心")
    @TableField("PROFIT_CENTER")
    private String profitCenter;

    @Schema(name = "考核指标")
    @TableField("PERFORMANCE_INDICATORS")
    private String performanceIndicators;

    @Schema(name = "考核周期")
    @TableField("ASSESSMENT_CYCLE")
    private String assessmentCycle;

    @Schema(name = "上级汇报关系")
    @TableField("REPORTING_RELATIONSHIP")
    private String reportingRelationship;

    @Schema(name = "协作关系")
    @TableField("COLLABORATION_RELATIONSHIP")
    private String collaborationRelationship;

    @Schema(name = "监督关系")
    @TableField("SUPERVISION_RELATIONSHIP")
    private String supervisionRelationship;

    @Schema(name = "组织效率评分")
    @TableField("EFFICIENCY_SCORE")
    private String efficiencyScore;

    @Schema(name = "管理幅度")
    @TableField("MANAGEMENT_SPAN")
    private Integer managementSpan;

    @Schema(name = "层级合理性")
    @TableField("HIERARCHY_RATIONALITY")
    private String hierarchyRationality;

    @Schema(name = "协调成本")
    @TableField("COORDINATION_COST")
    private String coordinationCost;

    @Schema(name = "组织文化")
    @TableField("ORGANIZATIONAL_CULTURE")
    private String organizationalCulture;

    @Schema(name = "发展规划")
    @TableField("DEVELOPMENT_PLAN")
    private String developmentPlan;

    @Schema(name = "变更历史")
    @TableField("CHANGE_HISTORY")
    private String changeHistory;

    @Schema(name = "变更原因")
    @TableField("CHANGE_REASON")
    private String changeReason;

    @Schema(name = "变更影响")
    @TableField("CHANGE_IMPACT")
    private String changeImpact;

    @Schema(name = "审批状态")
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @Schema(name = "审批人")
    @TableField("APPROVER")
    private String approver;

    @Schema(name = "审批时间")
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    @Schema(name = "审批意见")
    @TableField("APPROVAL_COMMENTS")
    private String approvalComments;

    @Schema(name = "是否叶子节点")
    @TableField("IS_LEAF")
    private String isLeaf;

    @Schema(name = "子组织数量")
    @TableField("CHILD_COUNT")
    private Integer childCount;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @Schema(name = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @Schema(name = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
