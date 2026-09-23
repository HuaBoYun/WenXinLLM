package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@Schema(name="审计项目查询入参")
public class TblnbsjProjectVo extends BaseVo{
	
	@Schema(name = "项目名称")
    private String prjoectName;

	@Schema(name = "被审计单位名称")
    private String orgName;

	@Schema(name = "项目经理")
	private Integer pmId;
	
    @Schema(name = "项目来源")
    private String projectSource;

    @Schema(name = "状态:1启动 0未启动 2实施 3完成 4归档")
    private Integer status;
    
    @Schema(name="orgid",hidden=true)
    private BigDecimal orgId;
    
    @Schema(name="staffId",hidden=true)
    private BigDecimal staffId;
    
    @Schema(name = "计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
    
    @Schema(name="projectId",hidden=true)
    private BigDecimal projectId;
    
    @Schema(name="rolename",hidden=true)
    private String rolename;
    
    
    @Schema(name = "项目编号")
    private String projectCode;
    
    @Schema(name = "项目类别")
    private String auditType;
    
    @Schema(name = "年度")
    private String projectYear;
    
    @Schema(name = "整改方案用")
    private String isZgfa;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
    
}