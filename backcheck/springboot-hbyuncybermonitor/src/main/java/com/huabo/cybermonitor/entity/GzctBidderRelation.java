package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_BIDDER_RELATION")
public class GzctBidderRelation {
    @TableId
    private String id;
    private String bidderA;
    private String bidderB;
    private String relationType;
    private String strength;
    private String projectId;
    private LocalDate discoveryDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
