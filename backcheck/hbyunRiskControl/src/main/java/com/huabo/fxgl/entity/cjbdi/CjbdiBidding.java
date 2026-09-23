package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 招投标信息（接口10）
 */
@Data
@TableName("TBL_CJBDI_BIDDING")
@KeySequence(value = "SEQ_CJBDI_BIDDING", dbType = DbType.ORACLE)
public class CjbdiBidding implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("TITLE")
    private String title;

    @TableField("BID_TYPE")
    private String bidType;

    @TableField("PUBLISH_DATE")
    private String publishDate;

    @TableField("REGION")
    private String region;

    @TableField("PURCHASER")
    private String purchaser;

    @TableField("AMOUNT")
    private String amount;

    @TableField("CONTENT_URL")
    private String contentUrl;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

