package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_EXISTING_PLAN_LINK")
@Schema(name="测试计划关联现行标准实体类")
public class TblExistingPlanLink {
    private static final long serialVersionUID = 1L;
    @Schema(name = "测试计划ID")
    @TableField("TASKID")
    private BigDecimal taskId;

    @Schema(name = "现行标准ID")
    @TableField("TESTID")
    private BigDecimal testId;

}
