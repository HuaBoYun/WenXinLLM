package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_PROCUREMENT_PROJECT")
public class GzctProcurementProject {

    @TableId
    private String id;

    private String projectName;

    private String procurementMethod;

    private BigDecimal budgetAmount;

    private BigDecimal winningAmount;

    private String winningSupplier;

    private String projectStatus;

    private String companyName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;
}
