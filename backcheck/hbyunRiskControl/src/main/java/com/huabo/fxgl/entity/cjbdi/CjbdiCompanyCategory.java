package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 企业-数据类别关联
 */
@Data
@TableName("TBL_CJBDI_COMPANY_CATEGORY")
@KeySequence(value = "SEQ_CJBDI_COMPANY_CAT", dbType = DbType.ORACLE)
public class CjbdiCompanyCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("COMPANY_ID")
    private Long companyId;

    @TableField("CATEGORY_ID")
    private Long categoryId;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

