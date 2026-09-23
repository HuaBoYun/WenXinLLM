package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_BIDDING_COMPLIANCE")
public class GzctBiddingCompliance {
    @TableId
    private String id;
    private String projectNo;
    private String projectName;
    private String companyName;
    private String projectType;
    private String biddingMethod;
    private BigDecimal contractAmount;
    private BigDecimal biddingThreshold;
    private String isAboveThreshold;
    private String complianceStatus;
    private String violationType;
    private LocalDate approvalDate;
    private String riskLevel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
