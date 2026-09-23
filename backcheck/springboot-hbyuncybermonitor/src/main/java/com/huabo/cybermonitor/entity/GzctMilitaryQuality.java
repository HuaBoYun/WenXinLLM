package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_QUALITY")
public class GzctMilitaryQuality extends Model<GzctMilitaryQuality> {

    @TableId(value = "QUALITY_ID", type = IdType.ASSIGN_UUID)
    private String qualityId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("INSPECTION_ITEM")
    private String inspectionItem;

    @TableField("INSPECTION_DATE")
    private LocalDate inspectionDate;

    @TableField("INSPECTION_RESULT")
    private String inspectionResult;

    @TableField("ISSUE_COUNT")
    private Integer issueCount;

    @TableField("ISSUE_DESC")
    private String issueDesc;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("BATCH_NO")
    private String batchNo;

    @TableField("INSPECTOR_NAME")
    private String inspectorName;

    @TableField("RECTIFICATION_STATUS")
    private String rectificationStatus;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
