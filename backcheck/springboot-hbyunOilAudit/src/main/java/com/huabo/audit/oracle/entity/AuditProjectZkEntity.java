package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Rui
 * @ClassName AuditProjectZk
 * @Description
 * @DATE 2023/10/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_YQNS_PROJECT_ZK")
@Schema(name="审计项目追款")
public class AuditProjectZkEntity {
	

    @Schema(name = "ID")
    @TableId("ID")
    @Id
    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
    private Integer id;

	@TableField("RESULT_ID")
	@Schema(name = "审计结果确认单Id")
	private String resultId;
	
	@Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

	@TableField(exist = false)
	private TblYqnsResult result;

    @TableField("PROJECT_NAME")
    @Schema(name = "项目名称")
    private String projectName;


    @TableField(value = "AUDIT_ORG_ID")
	@Schema(name = "被审计单位ID")
	private String auditOrgId;

	@TableField(exist = false)
	private TblOrganization auditOrg;

	@TableField(value = "ZK_ORG_ID")
	@Schema(name = "追款相对方(审计结果中施工单位)")
	private String zkOrgId;

	@TableField(exist = false)
	private TblOrganization zkOrg;
	

	@TableField(value = "REASON")
	@Schema(name = "追款事由")
	private String reason;
	
	@TableField(value = "MONEY")
	@Schema(name = "追款金额(审计结果中审计认定金额)")
	private BigDecimal money;
	
	
	@TableField(value = "REMARK")
	@Schema(name = "备注")
	private String remark;

	@TableField(value = "PROJECTID")
	@Schema(name = "关联项目-id")
	private BigDecimal projectId;

	@TableField(value = "CREATORSTAFFID")
	@Schema(name = "创建人id")
	private BigDecimal creatorStaffId;

	@Schema(name = "审批状态 1-审批中、2-需调整、6-已完成")
	@TableField("STATUS")
	private Integer status;

	@TableField(value = "RESERVED1")
	@Schema(name = "预留字段")
	private String RESERVED1;
}
