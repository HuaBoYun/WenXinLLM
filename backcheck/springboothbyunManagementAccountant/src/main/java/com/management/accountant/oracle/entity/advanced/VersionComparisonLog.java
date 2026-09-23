package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_VERSION_COMPARISON_LOG")
public class VersionComparisonLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String logId;
    private String comparisonId;
    private Date logTime;
    private String operation;
    private String operator;
    private String description;
    private String result;
    private Date createTime;
    private Integer delFlag;
}
