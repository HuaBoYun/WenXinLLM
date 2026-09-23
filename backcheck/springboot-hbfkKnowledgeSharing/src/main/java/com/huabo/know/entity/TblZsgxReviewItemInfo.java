package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Getter
@Setter
@TableName("TBL_ZSGX_REVIEW_ITEM_INFO")
@Schema(name="TblZsgxReviewItemInfo对象")
public class TblZsgxReviewItemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("REVIEW_POINT_NAME")
    private String reviewPointName;

    @TableField("REVIEW_ID")
    private String reviewId;

    @TableField("REVIEW_ITEM_ID")
    private String reviewItemId;

    @TableField("REVIEW_ITEM_NAME")
    private String reviewItemName;

    @TableField("ITEM_DESCRIPTION")
    private String itemDescription;

    @TableField("ITEM_SAMPLE")
    private String itemSample;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("REVIEW_MODEL")
    private String reviewModel;

    @TableField("RISK_WARNING")
    private String riskWarning;

    @TableField("RELATED_REGULATIONS")
    private String relatedRegulations;

    @TableField("REVIEW_CONTENT")
    private String reviewContent;

    @TableField("JUDGE_CONDITIONS")
    private String judgeConditions;

    @TableField("CREATE_COMPANY")
    private String createCompany;

    @TableField("CREATE_DEPT")
    private String createDept;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("DELETED")
    private Integer deleted;


}
