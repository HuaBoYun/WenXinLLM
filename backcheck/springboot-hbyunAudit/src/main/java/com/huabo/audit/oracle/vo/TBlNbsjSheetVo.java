package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="我的底稿列表查询入参")
public class TBlNbsjSheetVo extends BaseVo{
	@Schema(name = "底稿名称")
    private String sheetname;

    @Schema(name = "底稿编号")
    private String sheetcode;
    
    @Schema(name = "审核状态")
    private String status;
    
    @Schema(name = "拟稿人")
    private String staffid;
    
    @Schema(name = "项目id")
    private BigDecimal projectid;
    
    @Schema(name = "项目名称")
    private String projectName;
    
    @Schema(name = "项目编号")
    private String projectNo;
    
    @Schema(name = "审计对象组织/被审计单位组织id")
    private String auditOrgId;
    
    @Schema(name = "审计对象组织/被审计单位组织名称")
    private String orgName;
    
    @Schema(name = "审计对象人")
    private BigDecimal auditStaffId;
    
    @Schema(name = "关联任务id")
    private  BigDecimal operateid;
    
    @Schema(name = "审计事项")
    private String bsusinessAffiliation;
    
    @Schema(name = "问题类型")
    private String internalType;
    
    @Schema(name = "开始时间")
    private String startDate;
    
    @Schema(name = "结束时间")
    private String endDate;

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