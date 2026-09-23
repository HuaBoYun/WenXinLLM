package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * CJBDI 查询结果主表
 */
@Data
@TableName("TBL_CJBDI_QUERY_RESULT")
@KeySequence(value = "SEQ_CJBDI_QUERY_RESULT", dbType = DbType.ORACLE)
public class CjbdiQueryResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "RESULT_ID", type = IdType.INPUT)
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("CREDIT_CODE")
    private String creditCode;

    @TableField("CATEGORY_ID")
    private Long categoryId;

    @TableField("QUERY_TIME")
    private Timestamp queryTime;

    @TableField("DATA_COUNT")
    private Integer dataCount;

    @TableField("RAW_JSON")
    private String rawJson;

    @TableField("STATUS")
    private Integer status;

    @TableField("ERROR_MSG")
    private String errorMsg;

    @TableField("STAFF_ID")
    private Long staffId;

    @TableField("ORG_ID")
    private Long orgId;

    /** 查询到的明细数据（不持久化，仅用于接口返回） */
    @TableField(exist = false)
    private Object detailItems;
}

