package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_VERSION_COMPARISON")
public class VersionComparison implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String comparisonId;
    private String comparisonName;
    private String comparisonType;
    private String sourceVersion;
    private String targetVersion;
    private String comparisonScope;
    private String comparisonStatus;
    private Integer differenceCount;
    private String description;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer delFlag;
}
