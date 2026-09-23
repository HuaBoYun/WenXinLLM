package com.huabo.audit.oracle.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 描述: 计划编号
 * author: lyz
 * date: 2022-04-13
 */
@TableName("TBL_NBSJ_AUDITPLAN")
@Data
@Schema(name="计划编号实体类")
@Accessors(chain = true)
public class TblNbsjAuditplanEntity{
	
	public final static Integer NO_SHS=0;//未实施
	public final static Integer YE_SHS=1;//已实施
	////审批状态
	/**
	 * 0未审批 
	 */
	public final static Integer SPNO=0;
	/**
	 * 1 审批中
	 */
	public final static Integer SPKA=1;
	/**
	 * 2 需调整  
	 */
	public final static Integer SPTZ=2;
	/**
	 * 3审批完
	 */
	public final static Integer SPWC=3;
	public final static Integer SPZZ=4;
	public final static Integer YSCXM=5;//已生成项目

    @TableId(value = "planid")
    private BigDecimal planid;

    @TableField(value = "plancode")
    @Schema(name = "计划编码")
    private String plancode;

    @TableField(value = "planname")
    @Schema(name = "计划名称")
    private String planname;

    @TableField(value = "palnyear")
    @Schema(name = "计划年度")
    private String palnyear;

    @TableField(value = "plantype")
    @Schema(name = "计划类型")
    private String plantype;

    @TableField(value = "auditorgid")
    @Schema(name = "计划对象")
    private Double auditorgid;

    @TableField(value = "palncost")
    @Schema(name = "计划估算费用")
    private Double palncost;

    @TableField(value = "starttime")
    @Schema(name = "开始时间")
    private Date starttime;

    @TableField(value = "endtime")
    @Schema(name = "结束时间")
    private Date endtime;

    @TableField(value = "principalid")
    @Schema(name = "计划负责人")
    private BigDecimal principalid;

    @TableField(value = "leaderid")
    @Schema(name = "审计组长")
    private BigDecimal leaderid;

    @TableField(value = "remarks")
    @Schema(name = "审计目标和范围")
    private String remarks;

    @TableField(value = "createstaffid")
    @Schema(name = "创建人")
    private BigDecimal createstaffid;

    @TableField(value = "createtime")
    @Schema(name = "创建时间")
    private Date createtime;

    @TableField(value = "updatetimr")
    @Schema(name = "修改时间")
    private Date updatetimr;

    @TableField(value = "status")
    @Schema(name = "项目状态")
    private Double status;

    @TableField(value = "opinionstatus")
    @Schema(name = "审批状态")
    private Double opinionstatus;

    @TableField(value = "isauditor")
    @Schema(name = "未知")
    private String isauditor;


}