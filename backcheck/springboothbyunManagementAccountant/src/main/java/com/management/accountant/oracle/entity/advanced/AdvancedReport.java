package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_ADVANCED_REPORT")
public class AdvancedReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String reportId;
    private String reportName;
    private String reportType;
    private String dataSource;
    private String generationFrequency;
    private String reportStatus;
    private String description;
    private String createBy;
    private Date createTime;
   private Date lastGeneratedTime;
   private Integer subscriberCount;
    private String updateBy;
    private Date updateTime;
    private Integer delFlag;
}
