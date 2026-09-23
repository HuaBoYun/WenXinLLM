package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 工商基础信息（接口03）
 */
@Data
@TableName("TBL_CJBDI_BUSINESS_INFO")
@KeySequence(value = "SEQ_CJBDI_BUSINESS_INFO", dbType = DbType.ORACLE)
public class CjbdiBusinessInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("CREDIT_CODE")
    private String creditCode;

    @TableField("ENT_NAME")
    private String entName;

    @TableField("LEGAL_PERSON")
    private String legalPerson;

    @TableField("REG_CAPITAL")
    private String regCapital;

    @TableField("ESTABLISH_DATE")
    private String establishDate;

    @TableField("ENT_STATUS")
    private String entStatus;

    @TableField("ENT_TYPE")
    private String entType;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("ADDRESS")
    private String address;

    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    @TableField("REG_AUTHORITY")
    private String regAuthority;

    @TableField("APPROVAL_DATE")
    private String approvalDate;

    @TableField("BUSINESS_FROM")
    private String businessFrom;

    @TableField("BUSINESS_TO")
    private String businessTo;

    /** 完整子模型JSON（PERSON/ENTINV/ALTER等16个子模型） */
    @TableField("DATA_JSON")
    private String dataJson;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

