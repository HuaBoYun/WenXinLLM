package com.huabo.fxgl.entity.cjbdi;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * CJBDI 外部数据类别配置
 */
@Data
@TableName("TBL_CJBDI_DATA_CATEGORY")
@KeySequence(value = "SEQ_CJBDI_CATEGORY", dbType = DbType.ORACLE)
public class CjbdiDataCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CATEGORY_ID", type = IdType.INPUT)
    private Long categoryId;

    @TableField("CATEGORY_NAME")
    private String categoryName;

    @TableField("CATEGORY_GROUP")
    private String categoryGroup;

    @TableField("API_CODE")
    private String apiCode;

    @TableField("API_PATH")
    private String apiPath;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /** 该接口专用的ticket凭证 */
    @TableField("TICKET")
    private String ticket;

    /** 该接口专用的AES加密密钥 */
    @TableField("AES_KEY")
    private String aesKey;

    /** 该接口的完整URL（可选，为空时使用baseUrl+apiPath拼接） */
    @TableField("API_URL")
    private String apiUrl;

    /**
     * 接口登录账号（仅部分接口需要，如企业纠纷 apiCode=24）
     * 不映射数据库字段，通过 CjbdiConfig 配置注入
     */
    @TableField(exist = false)
    private String loginName;

    /**
     * 接口登录密码（仅部分接口需要，如企业纠纷 apiCode=24）
     * 不映射数据库字段，通过 CjbdiConfig 配置注入
     */
    @TableField(exist = false)
    private String loginPassword;

    @TableField("CREATE_TIME")
    private Timestamp createTime;
}

