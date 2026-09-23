package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 涉诉信息（接口20 - 法眼涉诉模型）
 */
@Data
@TableName("TBL_CJBDI_LITIGATION")
@KeySequence(value = "SEQ_CJBDI_LITIGATION", dbType = DbType.ORACLE)
public class CjbdiLitigation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("CASE_TYPE")
    private String caseType;

    @TableField("CASE_NO")
    private String caseNo;

    @TableField("CASE_REASON")
    private String caseReason;

    @TableField("COURT_NAME")
    private String courtName;

    @TableField("JUDGE_DATE")
    private String judgeDate;

    @TableField("ROLE_TYPE")
    private String roleType;

    @TableField("AMOUNT_LEVEL")
    private String amountLevel;

    @TableField("CASE_STATUS")
    private String caseStatus;

    @TableField("CONTENT")
    private String content;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

