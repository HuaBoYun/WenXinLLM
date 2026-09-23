package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.oracle.entity.base.ReservedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName BalanceProjectEntity
 * @Description
 * @DATE 2023/10/01
 */

@Data
@TableName("TBL_YQNS_BALANCE_PROJECT")
@Schema(name="工程结算项目信息情况表")
@Accessors(chain = true)
public class BalanceProjectEntity extends ReservedEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="PROJECT_NAME")
    @Schema(name="项目名称")
    private String projectName;

    @TableField(value="PLAN_NO")
    @Schema(name="计划文号")
    private String planNo;

    @TableField(value="CONTRACT_NO")
    @Schema(name="合同编号")
    private String contractNo;

    @TableField(value="CONSTRUCTION_CONTENT")
    @Schema(name="主要施工内容")
    private String constructionContent;

    @TableField(value="SETTLEMENT_AMOUNT")
    @Schema(name="结算金额")
    private BigDecimal settlementAmount;

    @TableField(value="MATERIAL_AMOUNT")
    @Schema(name="甲供物资金额")
    private BigDecimal materialAmount;

    @TableField(value="SURVEY_ORG_ID")
    @Schema(name="勘察单位")
    private String surveyOrgId;

	@TableField(exist = false)
	private TblOrganization surveyOrg;

    @TableField(value="DESIGN_ORG_ID")
    @Schema(name="设计单位")
    private String designOrgId;

	@TableField(exist = false)
	private TblOrganization designOrg;

    @TableField(value="CONSTRUCT_ORG_ID")
    @Schema(name="施工单位")
    private String constructOrgId;

	@TableField(exist = false)
	private TblOrganization constructOrg;

    @TableField(value="SUPERVISION_ORG_ID")
    @Schema(name="监理单位")
    private String supervisionOrgId;

	@TableField(exist = false)
	private TblOrganization supervisionOrg;

    @TableField(value="BUILD_MANAGER")
    @Schema(name="建设单位项目经理")
    private String buildManager;

    @TableField(value="BUILD_MANAGER_PHONE")
    @Schema(name="建设单位项目经理电话")
    private String buildManagerPhone;

    @TableField(value="MATERIAL_MANAGER")
    @Schema(name="项目物资负责人")
    private String materialManager;

    @TableField(value="MATERIAL_MANAGER_PHONE")
    @Schema(name="项目物资负责人电话")
    private String materialManagerPhone;

    @TableField(value="CONSTRUCT_MANAGER")
    @Schema(name="施工单位项目经理")
    private String constructManager;

    @TableField(value="CONSTRUCT_MANAGER_PHONE")
    @Schema(name="施工单位项目经理电话")
    private String constructManagerPhone;

    @TableField(value="MANAGER")
    @Schema(name="总监")
    private String manager;

    @TableField(value="MANAGER_PHONE")
    @Schema(name="总监及电话")
    private String managerPhone;

    @TableField(value="DESIGN_MANAGER")
    @Schema(name="设计负责人")
    private String designManager;

    @TableField(value="DESIGN_MANAGER_PHONE")
    @Schema(name="设计负责人电话")
    private String designManagerPhone;

    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

}
