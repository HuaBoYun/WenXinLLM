package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_BIDDING_MONITOR")
public class GzctBiddingMonitor {

    @TableId
    private String id;

    private String projectName;

    private String biddingType;

    private BigDecimal budgetAmount;

    private Integer bidderCount;

    private Integer fileSimilarity;

    private String collusionRisk;

    private String priceAnalysis;

    private String suggestion;

    private LocalDate biddingDate;

    private String companyName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;
}
