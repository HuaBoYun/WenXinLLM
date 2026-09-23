package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("TBL_VERSION_COMPARISON_DIFF")
public class VersionComparisonDiff implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    private String diffId;
    private String comparisonId;
    private String fieldName;
    private String fieldPath;
    private String sourceValue;
    private String targetValue;
    private String differenceType;
    private String impact;
    private String recommendation;
    private Date createTime;
    private Integer delFlag;
}
