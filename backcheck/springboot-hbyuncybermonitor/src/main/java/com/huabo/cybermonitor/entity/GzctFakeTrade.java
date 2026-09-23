package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_FAKE_TRADE")
public class GzctFakeTrade {
    @TableId
    private String id;
    private String tradeNo;
    private String tradeName;
    private String counterparty;
    private BigDecimal contractAmount;
    private String checkStatus;
    private String fiveFlowStatus;
    private LocalDateTime checkTime;
    private String checker;
    private String checkResult;
    private String companyName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
