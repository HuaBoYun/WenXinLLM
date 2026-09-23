package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型版本审批记录表实体类
 * 
 * @author 华博云
 * @date 2025-09-30
 */
@Data
@Accessors(chain = true)
@TableName("TBL_MODEL_VERSION_APPROVAL")
@Schema(name="TblModelVersionApproval对象", description="模型版本审批记录表")
public class TblModelVersionApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "审批ID")
    @TableId(value = "APPROVAL_ID", type = IdType.ASSIGN_ID)
    private String approvalId;

    @Schema(name = "版本ID")
    @TableField("VERSION_ID")
    private String versionId;

    @Schema(name = "审批类型(PUBLISH/ARCHIVE)")
    @TableField("APPROVAL_TYPE")
    private String approvalType;

    @Schema(name = "审批状态(PENDING/APPROVED/REJECTED)")
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @Schema(name = "提交人")
    @TableField("SUBMITTER")
    private String submitter;

    @Schema(name = "提交时间")
    @TableField("SUBMIT_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    @Schema(name = "审批人")
    @TableField("APPROVER")
    private String approver;

    @Schema(name = "审批时间")
    @TableField("APPROVAL_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalTime;

    @Schema(name = "审批意见")
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    @Schema(name = "拒绝原因")
    @TableField("REJECTION_REASON")
    private String rejectionReason;

    /**
     * 审批类型枚举
     */
    public static class ApprovalType {
        public static final String PUBLISH = "PUBLISH";    // 发布审批
        public static final String ARCHIVE = "ARCHIVE";    // 归档审批
    }

    /**
     * 审批状态枚举
     */
    public static class ApprovalStatus {
        public static final String PENDING = "PENDING";    // 待审批
        public static final String APPROVED = "APPROVED";  // 已通过
        public static final String REJECTED = "REJECTED";  // 已拒绝
    }
}
