package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseProjectEntity;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TBL_YQNS_PROJECT_WEEKLY
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_YQNS_PROJECT_WEEKLY")
@Schema(name="TblYqnsProjectWeerkly对象", description="审计项目周报表")
public class TblYqnsProjectWeerklyEntity extends BaseReservedProperty implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(name = "主键")
    @TableField("ID")
    private BigDecimal id;

    /**
     * 本周工作
     */
    @Schema(name = "本周工作")
    @TableField("WEEKWORK")
    private String weekWork;

    /**
     * 下周工作
     */
    @Schema(name = "下周工作")
    @TableField("NEXTWEEKWORK")
    private String nextWeekWork;

    /**
     * 其他事项
     */
    @Schema(name = "其他事项")
    @TableField("OTHERMATTERS")
    private String otherMatters;

    /**
     * 周报时间
     */
    @Schema(name = "周报时间")
    @TableField("WEEKDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date weekDate;

    /**
     * 填报时间
     */
    @Schema(name = "填报时间")
    @TableField("FILLTIME")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fillTime;

    /**
     * 填报时间
     */
    @Schema(name = "现场实际结束时间")
    @TableField("ENDTIMESITE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTimeSite;
    
    /**
     * 创建人id
     */
    @Schema(name = "创建人id")
    @TableField("CREATORID")
    private BigDecimal creatorId;

    /**
     * 创建人名称
     */
    @Schema(name = "创建人名称")
    @TableField("CREATORNAME")
    private String creatorName;

    /**    
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("CREATORDATE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creatorDate;

    /**
     * 备份字段
     */
    @Schema(name = "备份字段")
    @TableField("TEXT")
    private String text;

    /**
     * 实施方案id
     */
    @Schema(name = "实施方案id")
    @TableField("IMPLEMENTID")
    private BigDecimal implementId;


    /**
              * 实施方案
     */
    @Schema(name = "实施方案")
    @TableField(exist = false,typeHandler = JacksonTypeHandler.class)
    private ImplementPlanEntity implementPlanEntities ;


    
    @Schema(name = "审批状态")
    @TableField(value = "STATUS")
    private Integer status;

}