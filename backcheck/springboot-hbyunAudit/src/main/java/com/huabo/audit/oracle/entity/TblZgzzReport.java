package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author LHP
 * @since 2024-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ZGZZ_REPORT")
@Table(name = "TBL_ZGZZ_REPORT")
@Schema(name="TblZgzzReport对象", description="整改报告")
public class TblZgzzReport implements Serializable {

	/**
	 *  TBL_ZGZZ_REPORTPLAN  --整改报告关联整改方案关系表
	 *  TBL_ZGZZ_REPORTISSUES  --整改报告关联整改落实关系表
	 *  TBL_ZGZZREPORT_ATT   --整改报告关联附件关系表
	 */
	
    private static final long serialVersionUID = 1L;

    @Schema(name = "整改报告主键")
    @TableId("REPORTID")
    @Column(name = "REPORTID")
    @Id
    private String reportid;

    @Schema(name = "整改报告编号")
    @TableField("REPORTCODE")
    @Column(name = "REPORTCODE")
    private String reportcode;

    @Schema(name = "整改报告名称")
    @TableField("REPORTNAME")
    @Column(name = "REPORTNAME")
    private String reportname;

    @Schema(name = "报告类型 1-整改方案报告，2整改落实报告")
    @TableField("REPORTTYPE")
    @Column(name = "REPORTTYPE")
    private BigDecimal reporttype;

    @Schema(name = "创建人")
    @TableField("CREATESTAFF")
    @Column(name = "CREATESTAFF")
    private BigDecimal createstaff;

    @Schema(name = "报告编制部门")
    @TableField("LINKDEPT")
    @Column(name = "LINKDEPT")
    private BigDecimal linkdept;

    @Schema(name = "创建时间")
    @TableField("CREATEDATE")
    @Column(name = "CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;

    @Schema(name = "所属公司")
    @TableField("LINKORG")
    @Column(name = "LINKORG")
    private BigDecimal linkorg;

    @Schema(name = "报告内容")
    @TableField("REPORTCONTECT")
    @Column(name = "REPORTCONTECT")
    private String reportcontect;
    
    @Schema(name = "审批状态 0-未评价 1-审批中 2-已退回 3-已撤销 6-已完成")
    @TableField("STATUS")
    @Column(name = "STATUS")
    private Integer status;
    
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    @Column(name = "STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    @Column(name = "STAFFSCOPENAMES")
    private String staffScopeNames;
    
}
