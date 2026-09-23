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
 * @ClassName SubContractEntity
 * @Description
 * @DATE 2023/10/01
 */

@Data
@TableName("TBL_YQNS_SUB_CONTRACT")
@Schema(name="分包情况调查表")
@Accessors(chain = true)
public class SubContractEntity extends ReservedEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="PROJECT_NAME")
    @Schema(name="分包项目名称")
    private String projectName;

    @TableField(value="GENERAL_CONTRACT_NO")
    @Schema(name="对应的总包含合同编号")
    private String generalContractNo;

    @TableField(value="SUB_CONTRACT_NO")
    @Schema(name="分包合同编号")
    private String subContractNo;

    @TableField(value="SUB_ORG_ID")
    @Schema(name="分包单位")
    private String subOrgId;

	@TableField(exist = false)
	private TblOrganization subOrg;

    @TableField(value="SUB_TYPE")
    @Schema(name="分包形式")
    private String subType;

    @TableField(value="SUB_CONTRACT_AMOUNT")
    @Schema(name="分包合同金额")
    private BigDecimal subContractAmount;

    @TableField(value="SUB_SETTLEMENT_AMOUNT")
    @Schema(name="分包结算金额")
    private BigDecimal subSettlementAmount;

    @TableField(value="QUOTA")
    @Schema(name="额度")
    private BigDecimal quota;

    @TableField(value="PROJECT_ADDRESS")
    @Schema(name="项目所在地")
    private String projectAddress;

    @TableField(value="SUB_CHECK_MODE")
    @Schema(name="分包选商方式")
    private String subCheckMode;

    @TableField(value="HAS_PROCEEDINGS")
    @Schema(name="是否涉及法律诉讼 1:是 0：否")
    private Integer hasProceedings;

    @TableField(value="BUILD_ORG_ID")
    @Schema(name="建设单位")
    private String buildOrgId;

	@TableField(exist = false)
	private TblOrganization buildOrg;

    @TableField(value="GENERAL_ORG_ID")
    @Schema(name="总包单位")
    private String generalOrgId;

	@TableField(exist = false)
	private TblOrganization generalOrg;

    @TableField(value="AUDIT_PERSON_ID")
    @Schema(name="审计人员")
    private String auditPersonId;

	@TableField(exist = false)
	private TblStaff auditPerson;


}
