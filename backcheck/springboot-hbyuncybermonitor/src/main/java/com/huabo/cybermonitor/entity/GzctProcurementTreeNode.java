package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_PROCUREMENT_TREE_NODE")
public class GzctProcurementTreeNode {
    @TableId
    private String id;
    private String nodeName;
    private String parentId;
    private Integer nodeLevel;
    private String riskLevel;
    private BigDecimal purchaseTotal;
    private BigDecimal relatedRatio;
    private Integer warnCount;
    private String companyId;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
