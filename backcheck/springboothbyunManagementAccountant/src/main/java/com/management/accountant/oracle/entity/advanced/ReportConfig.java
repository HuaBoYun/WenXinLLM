package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_REPORT_CONFIG")
public class ReportConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String configId;
    private String reportId;
    private String queryConditions;
    private String fieldConfiguration;
    private String sortRules;
    private String groupSettings;
    private String formatSettings;
    private Date createTime;
    private Date updateTime;
    private Integer delFlag;
}
