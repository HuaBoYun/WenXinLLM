package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 失信记录（接口21）
 */
@Data
@TableName("TBL_CJBDI_DISHONESTY")
@KeySequence(value = "SEQ_CJBDI_DISHONESTY", dbType = DbType.ORACLE)
public class CjbdiDishonesty implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("CASE_NO")
    private String caseNo;

    @TableField("COURT_NAME")
    private String courtName;

    @TableField("AREA_NAME")
    private String areaName;

    @TableField("DUTY")
    private String duty;

    @TableField("PERFORMANCE")
    private String performance;

    @TableField("DISHONESTY_TYPE")
    private String dishonestyType;

    @TableField("PUBLISH_DATE")
    private String publishDate;

    @TableField("REG_DATE")
    private String regDate;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

