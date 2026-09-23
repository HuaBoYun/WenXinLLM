package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 通用风险数据宽表
 * 覆盖：环保处罚(13)、欠税(14)、动产抵押(15)、股权出质(16)、股权冻结(17)、
 *       军采黑名单(18)、政采黑名单(19)、限高(22)、不良记录(23)、企业纠纷(24)、一般纳税人(08)
 */
@Data
@TableName("TBL_CJBDI_RISK_DATA")
@KeySequence(value = "SEQ_CJBDI_RISK_DATA", dbType = DbType.ORACLE)
public class CjbdiRiskData implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("CATEGORY_ID")
    private Long categoryId;

    @TableField("ITEM_TITLE")
    private String itemTitle;

    @TableField("ITEM_DATE")
    private String itemDate;

    @TableField("ITEM_AMOUNT")
    private String itemAmount;

    @TableField("ITEM_STATUS")
    private String itemStatus;

    @TableField("ITEM_AUTHORITY")
    private String itemAuthority;

    @TableField("ITEM_DETAIL")
    private String itemDetail;

    @TableField("FIELD1")
    private String field1;

    @TableField("FIELD2")
    private String field2;

    @TableField("FIELD3")
    private String field3;

    @TableField("FIELD4")
    private String field4;

    @TableField("FIELD5")
    private String field5;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

