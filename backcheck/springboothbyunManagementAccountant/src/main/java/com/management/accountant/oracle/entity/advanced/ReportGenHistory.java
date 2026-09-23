package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_REPORT_GEN_HISTORY")
public class ReportGenHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String historyId;
    private String reportId;
    private String generateType;
    private Date generateTime;
    private Integer dataRows;
    private String fileSize;
    private String generateDuration;
    private String status;
    private String errorMessage;
    private Date createTime;
    private Integer delFlag;
}
