package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 行政处罚信息（接口12）
 */
@Data
@TableName("TBL_CJBDI_ADMIN_PENALTY")
@KeySequence(value = "SEQ_CJBDI_ADMIN_PENALTY", dbType = DbType.ORACLE)
public class CjbdiAdminPenalty implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("PENALTY_NO")
    private String penaltyNo;

    @TableField("PENALTY_TYPE")
    private String penaltyType;

    @TableField("PENALTY_REASON")
    private String penaltyReason;

    @TableField("PENALTY_RESULT")
    private String penaltyResult;

    @TableField("PENALTY_AUTHORITY")
    private String penaltyAuthority;

    @TableField("PENALTY_DATE")
    private String penaltyDate;

    @TableField("PENALTY_AMOUNT")
    private String penaltyAmount;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

