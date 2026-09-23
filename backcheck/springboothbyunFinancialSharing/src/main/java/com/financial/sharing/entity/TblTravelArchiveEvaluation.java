package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评价记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TRAVEL_ARCHIVE_EVALUATION")
@ApiModel(value = "TblTravelArchiveEvaluation", description = "评价记录表")
public class TblTravelArchiveEvaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "EVALUATION_ID")
    @ApiModelProperty(value = "评价ID")
    private String evaluationId;

    @TableField("ARCHIVE_ID")
    @ApiModelProperty(value = "档案ID")
    private String archiveId;

    @TableField("SERVICE_SCORE")
    @ApiModelProperty(value = "服务评分")
    private Integer serviceScore;

    @TableField("PRICE_SCORE")
    @ApiModelProperty(value = "价格评分")
    private Integer priceScore;

    @TableField("ENVIRONMENT_SCORE")
    @ApiModelProperty(value = "环境评分")
    private Integer environmentScore;

    @TableField("OVERALL_SCORE")
    @ApiModelProperty(value = "综合评分")
    private BigDecimal overallScore;

    @TableField("CONTENT")
    @ApiModelProperty(value = "评价内容")
    private String content;

    @TableField("EVALUATION_USER")
    @ApiModelProperty(value = "评价人ID")
    private String evaluationUser;

    @TableField("EVALUATION_USER_NAME")
    @ApiModelProperty(value = "评价人姓名")
    private String evaluationUserName;

    @TableField("EVALUATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价时间")
    private LocalDateTime evaluationTime;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
