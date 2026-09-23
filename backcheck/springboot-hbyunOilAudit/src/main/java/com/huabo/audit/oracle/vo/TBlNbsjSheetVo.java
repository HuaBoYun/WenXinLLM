package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;

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
    private BigDecimal projectId;
    
    @Schema(name = "项目名称")
    private String projectName;
    
    @Schema(name = "审计对象组织/被审计单位组织id")
    private Integer auditOrgId;
    
    @Schema(name = "审计对象人")
    private Integer auditStaffId;
    
    @Schema(name = "关联任务id")
    private  Integer operateid;
    
    @Schema(name = "TblYqnsProjectAuditTemplate 主键")
    private Long templateId;
    
    @Schema(name = "被审计对象名称")
	private String orgidnames;
    
   	private String contractcode;
   	
   	private String contractname;
   	
   	private String contractmoney;
   	
   	@Schema(name = "问题简述")
	private String overview;
   	
   	
    @Schema(name="创建人id")
    private BigDecimal createstaff;

}