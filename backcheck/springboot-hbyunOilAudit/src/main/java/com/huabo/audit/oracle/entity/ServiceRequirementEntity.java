package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName ServiceRequirementEntity
 * @Description
 * @DATE 2023/9/6
 */
@Data
@TableName("TBL_YQNS_SERVICE_REQUIREMENT")
@Schema(name="服务需求表")
@Accessors(chain = true)
public class ServiceRequirementEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
	
	@TableField(value="REQUIREMENTNO")
	@Schema(name="编号")
	private BigDecimal requirementNo;
	
	@Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="AUDIT_ITEM")
    @Schema(name="审计事项")
    private String auditItem;

    @TableField(value="AUDIT_PURPOSE")
    @Schema(name="审计目的")
    private String auditPurpose;

    @TableField(value="UNIT_ID")
    @Schema(name="单位")
    private BigDecimal organizationId;

	@TableField(exist = false)
	private TblOrganization organization;

    @TableField(value="PROJECT_TYPE")
    @Schema(name="项目类型")
    private String projectType;

    @TableField(value="CONCERNS_CONTENT")
    @Schema(name="重点关注内容")
    private String concernsContent;

    @TableField(value="UNIT_RANGE")
    @Schema(name="单位范围")
    private String unitRange;

    @TableField(value="TIME_RANGE")
    @Schema(name="时间范围")
    private String timeRange;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;
    
    
    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门")
    private String queryDeptIds;
    
    @TableField(exist = false)
    @Schema(name="当前查询人")
    private BigDecimal currentStaffId;
    
    @TableField(exist = false)
    @Schema(name="创建年度")
    private Integer queryYear;

    @TableField(exist = false)
    @Schema(name="ids")
    private String ids;

}
