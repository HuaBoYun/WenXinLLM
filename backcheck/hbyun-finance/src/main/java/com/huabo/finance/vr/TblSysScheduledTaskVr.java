package com.huabo.finance.vr;

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
@Schema(name="TblSysScheduledTask对象", description="定时任务信息表")
public class TblSysScheduledTaskVr implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Schema(name = "主键")
    private String taskId;

    @Schema(name = "任务名称")
    private String taskName;

    @Schema(name = "Spring Bean名称")
    private String beanName;

    @Schema(name = "执行方法名")
    private String methodName;

    @Schema(name = "方法参数(JSON格式)")
    private String params;

    @Schema(name = "cron表达式")
    private String cornExpression;

    @Schema(name = "任务描述")
    private String descrtiption;

    @Schema(name = "状态: 1启用, 0停用")
    private Integer status;

    @Schema(name = "执行采集方案")
    private String planId;
      
    @Schema(name = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTIme;
    
    @Schema(name = "创建人")
    private BigDecimal createStaffId;
    
    @Schema(name = "创建人姓名")
    private String createStaffName;
    
    
    @Schema(name = "修改人")
    private BigDecimal modifyStaffId;
    
    @Schema(name = "修改人姓名")
    private String modifyStaffName;
    
    @Schema(name = "所属公司")
    private BigDecimal linkOrgId;
    
    @Schema(name = "所属部门")
    private BigDecimal linkDeptId;
    
    @Schema(name = "是否运行， true 正在运行， false 未运行")
    private boolean running;
    
    @Schema(name = "下一次运行时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date nextTime;
    
    @Schema(name = "筛选条件-方案名称")
    private String planName;
    
}
