package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("GZCT_PROCUREMENT_WARNING")
public class GzctProcurementWarning {
    @TableId
    private String id;
    private String warningNo;
    private String warningLevel;
    private String title;
    private String content;
    private String companyName;
    private LocalDateTime warningTime;
    private String status;
    private String handler;
    private LocalDateTime handleTime;
    private String handleResult;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
