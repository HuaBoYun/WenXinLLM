package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 舆情信息（接口11）
 */
@Data
@TableName("TBL_CJBDI_PUBLIC_OPINION")
@KeySequence(value = "SEQ_CJBDI_PUBLIC_OPINION", dbType = DbType.ORACLE)
public class CjbdiPublicOpinion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("RESULT_ID")
    private Long resultId;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("TITLE")
    private String title;

    @TableField("SOURCE")
    private String source;

    @TableField("PUBLISH_DATE")
    private String publishDate;

    @TableField("SENTIMENT")
    private String sentiment;

    @TableField("SUMMARY")
    private String summary;

    @TableField("CONTENT_URL")
    private String contentUrl;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

