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
import java.time.LocalDateTime;

/**
 * 账单识别日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BILL_RECOGNITION_LOG")
@ApiModel(value = "TblBillRecognitionLog", description = "账单识别日志表")
public class TblBillRecognitionLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @TableField("CONFIG_ID")
    @ApiModelProperty(value = "配置ID")
    private String configId;

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @TableField("BUSINESS_ID")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @TableField("BILL_IMAGE")
    @ApiModelProperty(value = "账单图片地址")
    private String billImage;

    @TableField("RECOGNITION_RESULT")
    @ApiModelProperty(value = "识别结果")
    private String recognitionResult;

    @TableField("OCR_RESULT")
    @ApiModelProperty(value = "OCR结果")
    private String ocrResult;

    @TableField("AUDIT_RESULT")
    @ApiModelProperty(value = "稽核结果")
    private String auditResult;

    @TableField("MATCH_RESULT")
    @ApiModelProperty(value = "匹配结果")
    private String matchResult;

    @TableField("RECOGNITION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "识别时间")
    private LocalDateTime recognitionTime;

    @TableField("RECOGNITION_USER")
    @ApiModelProperty(value = "识别人ID")
    private String recognitionUser;

    @TableField("RECOGNITION_STATUS")
    @ApiModelProperty(value = "识别状态")
    private String recognitionStatus;

    @TableField("ERROR_MESSAGE")
    @ApiModelProperty(value = "错误消息")
    private String errorMessage;
}
