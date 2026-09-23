package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务数据版本
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYS_SCHEDULEDTASK")
@Schema(name="TblSysScheduledTask对象", description="定时任务信息表")
public class TblSysScheduledTask implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FINANCEGATHERBEAN = "GatherFinanceDateService";
	public static final String FINANCEGATHERMETHOD = "beginCornFinanceTask";
	

	@Schema(name = "主键")
    @TableId("TASKID")
    private String taskId;

    @Schema(name = "任务名称")
    @TableField("TASKNAME")
    private String taskName;

    @Schema(name = "Spring Bean名称")
    @TableField("BEANNAME")
    private String beanName;

    @Schema(name = "执行方法名")
    @TableField("METHODNAME")
    private String methodName;

    @Schema(name = "方法参数(JSON格式)")
    @TableField("PARAMS")
    private String params;

    @Schema(name = "cron表达式")
    @TableField("CORNEXPRESSION")
    private String cornExpression;

    @Schema(name = "任务描述")
    @TableField("DESCRIPTION")
    private String descrtiption;

    @Schema(name = "状态: 1启用, 0停用")
    @TableField(value = "STATUS")
    private Integer status;

    @Schema(name = "执行采集方案")
    @TableField("PLANID")
    private String planId;
      
    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改时间")
    @TableField(value = "MODIFYTIME")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTIme;
    
    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createStaffId;
    
    @Schema(name = "修改人")
    @TableField(value = "MODIFYSTAFFID")
    private BigDecimal modifyStaffId;
    
    @Schema(name = "所属公司")
    @TableField(value = "LINKORGID")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属部门")
    @TableField(value = "LINKDEPTID")
    private BigDecimal linkDeptId;
    
    @Schema(name = "采集方案名称")
    @TableField(exist = false)
    private String planName;

}
