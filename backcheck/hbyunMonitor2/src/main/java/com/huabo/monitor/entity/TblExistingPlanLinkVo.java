package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="测试计划关联现行标准VO")
public class TblExistingPlanLinkVo {
    @Schema(name = "测试计划ID")
    private BigDecimal taskId;

    @Schema(name = "现行标准ID")
    private String testId;
}
