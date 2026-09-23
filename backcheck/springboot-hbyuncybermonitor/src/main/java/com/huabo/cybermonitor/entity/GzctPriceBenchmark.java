package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_PRICE_BENCHMARK")
public class GzctPriceBenchmark {
    @TableId
    private String id;
    private String itemName;
    private BigDecimal purchasePrice;
    private BigDecimal marketPrice;
    private BigDecimal deviationRate;
    private String supplierName;
    private LocalDate purchaseDate;
    private String companyName;
    private String category;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
