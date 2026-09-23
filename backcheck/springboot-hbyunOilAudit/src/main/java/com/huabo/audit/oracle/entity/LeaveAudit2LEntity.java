package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
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
 * @ClassName LeaveAudit2LEntity
 * @Description
 * @DATE 2023/9/14
 */
@Data
@TableName("TBL_YQNS_LEAVE_AUDIT_2L")
@Schema(name="二级单位及成员单位离任审计")
@Accessors(chain = true)
public class LeaveAudit2LEntity extends BaseReservedProperty implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @TableField(value="LEAVENO")
    @Schema(name="序号")
    private BigDecimal leaveNo;
    
    @TableField(value="NO")
    @Schema(name="编号")
    private String no;
    
    @TableField(value="PROJECT_NAME")
    @Schema(name="审计项目名称")
    private String projectName;

    @TableField(value="AUDIT_ORG")
    @Schema(name="被审计单位")
    private String auditOrgId;

	@TableField(exist = false)
	private TblOrganization auditOrg;

    @TableField(value="ENTRUST_NO")
    @Schema(name="委托书编号")
    private String entrustNo;

    @TableField(value="ENTRUST_TIME")
    @Schema(name="委托时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date entrustTime;

    @TableField(value="AUDIT_START_TIME")
    @Schema(name="审计任职起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date auditStartTime;

    @TableField(value="AUDIT_END_TIME")
    @Schema(name="审计任职结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date auditEndTime;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

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
    
    @TableField(value="AUDITSCOPE")
    @Schema(name="审计范围")
    private String auditScope;
    
    @TableField(value="PROJECTTYPE")
    @Schema(name="项目类型")
    private String projectType;
    
    @TableField(value="REMARKS")
    @Schema(name="备注")
    private String remarks;
    
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
    @Schema(name="关联id")
    private BigDecimal relaid;

    @Schema(name = "ids")
    @TableField(exist = false)
    private String ids;
    
    @TableField(exist = false)
    @Schema(name="被审计单位")
    private String auditOrgName;
}
