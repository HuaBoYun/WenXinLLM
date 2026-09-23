package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 投融资信息（接口09）
 */
@Data
@TableName("TBL_CJBDI_INVESTMENT")
@KeySequence(value = "SEQ_CJBDI_INVESTMENT", dbType = DbType.ORACLE)
public class CjbdiInvestment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("INVEST_ROUND")
    private String investRound;

    @TableField("INVEST_AMOUNT")
    private String investAmount;

    @TableField("INVEST_DATE")
    private String investDate;

    @TableField("INVESTOR")
    private String investor;

    @TableField("VALUATION")
    private String valuation;

    @TableField("RATIO")
    private String ratio;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

