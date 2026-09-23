package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.dto.FundAuditProjectAttDto;
import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TBL_YQNS_FUND_AUDIT_PROJECT
 * 财务审计项目
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_FUND_AUDIT_PROJECT")
@Schema(name="TblYqnsFundAuditProjectEntity对象")
public class TblYqnsFundAuditProjectEntity extends BaseProjectEntity implements Serializable {

    /**
     * 项目名称
     */
    @Schema(name = "项目名称")
    @TableField("NAME")
    private String name;
    
    

    /**
     * 小组
     */
    @Schema(name = "小组")
    @TableField("AUDITGROUP")
    private String auditGroup;

    /**
     * 审计范围
     */
    @Schema(name = "审计范围")
    @TableField("AUDITRANGE")
    private String auditRange;

    /**
     * 
     */
    @Schema(name = "状态")
    @TableField("STATUS")
    private String status;
    
    
    /**
     * 被审计单位
     */
    @Schema(name = "被审计单位")
    @TableField("AUDITUNIT")
    private String auditUnit;

    /**
     * 被审计单位Id
     */
    @Schema(name = "被审计单位ID")
    @TableField("AUDITUNITID")
    private Long auditUnitId;
    
    /**
     * 关联计划项目id
     */
    @Schema(name = "关联计划项目id")
    @TableField("GLJHXMID")
    private BigDecimal gljhxmid;
    
    @Schema(name = "关联计划项目类型")
    @TableField("GLJHXMLX")
    private String gljhxmlx;
    
    
    @Schema(name = "关联计划id")
    @TableField("PLANID")
    private BigDecimal planid;
    

    @Schema(name = "关联计划名称")
    @TableField("PLANNAME")
    private String planname;
    
    
    @Schema(name = "现在开始时间")
    @TableField("XCSRARTTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcsrarttime;

    
    @Schema(name = "现在结束时间")
    @TableField("XCENDTIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date xcendtime;
    
    
    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    
    @Schema(name = "下发项目组人员ids")
    @TableField("XFSMZRYIDS")
    private String xfsmzryids;
    
    
    @Schema(name = "下发项目组人员名称")
    @TableField("XFXMZRYNAMES")
    private String xfxmzrynames;
    
    
    @Schema(name = "下发专业科室负责人员ids")
    @TableField("XFKSRYIDS")
    private String xfksryids;
    
    
    @Schema(name = "下发专业科室负责人员名称")
    @TableField("XFKSRYNAMES")
    private String xfksrynames;
    
    
    @TableField("FZZSTAFFID")
    @Schema(name = "副组长主键")
    private BigDecimal fzzStafffId;
    
    @TableField("FZZNAME")
    @Schema(name = "副组长名称")
    private String fzzName;
    
    @Schema(name = "分配专业科室督导人员ids")
    @TableField("FPKSRYIDS")
    private String fpksryids;
    
    
    @Schema(name = "分配专业科室督导人员名称")
    @TableField("FPKSRYNAMES")
    private String fpksrynames;
    
    
    @Schema(name = "分配审理科人员主键id")
    @TableField("FPSLKRYID")
    private String fpslkryid;
    
    
    @Schema(name = "分配审理科人员人员名称")
    @TableField("FPSLKRYNAME")
    private String fpslkryname;
    
    
    @Schema(name = "编号")
    @TableField(value = "CODE")
    private String code;
    
    
    @Schema(name = "人员数量")
    @TableField("RSYQ")
    private Integer rsyq;
    
    @Schema(name = "项目数量")
    @TableField("XMSL")
    private Integer xmsl;
    
    
    @TableField(exist = false)
    @Schema(name = "关联附件")
    private List<FundAuditProjectAttDto> fundAuditProjectAttDtoList;
    
    
    @Schema(name = "下发状态（1已上报  2退回 0或null 未上报）")
    @TableField("XFSTATUS")
    private Integer xfstatus;
    
    
    
    
    private static final long serialVersionUID = 1L;
}