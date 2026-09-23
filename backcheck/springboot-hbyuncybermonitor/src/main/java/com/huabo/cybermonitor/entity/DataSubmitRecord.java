package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据报送记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_SUBMIT_RECORD")
public class DataSubmitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_UUID)
    private String recordId;

    /**
     * 任务ID
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 企业ID
     */
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    /**
     * 报送数据（JSON格式）
     */
    @TableField("SUBMIT_DATA")
    private String submitData;

    /**
     * 报送时间
     */
    @TableField("SUBMIT_TIME")
    private LocalDateTime submitTime;

    /**
     * 报送状态
     */
    @TableField("SUBMIT_STATUS")
    private String submitStatus;

    /**
     * 数据质量评分
     */
    @TableField("DATA_QUALITY_SCORE")
    private Integer dataQualityScore;

    /**
     * 质量检查结果
     */
    @TableField("QUALITY_CHECK_RESULT")
    private String qualityCheckResult;

    /**
     * 审核状态
     */
    @TableField("REVIEW_STATUS")
    private String reviewStatus;

    /**
     * 审核意见
     */
    @TableField("REVIEW_COMMENTS")
    private String reviewComments;

    /**
     * 审核人
     */
    @TableField("REVIEWER")
    private String reviewer;

    /**
     * 审核时间
     */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /**
     * 文件路径
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 文件大小
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 非数据库字段
    /**
     * 任务名称
     */
    @TableField(exist = false)
    private String taskName;

    /**
     * 企业名称
     */
    @TableField(exist = false)
    private String enterpriseName;

    // 报送状态常量
    public static final String STATUS_DRAFT = "DRAFT";           // 草稿
    public static final String STATUS_SUBMITTED = "SUBMITTED";   // 已提交
    public static final String STATUS_PROCESSING = "PROCESSING"; // 处理中
    public static final String STATUS_APPROVED = "APPROVED";     // 已通过
    public static final String STATUS_REJECTED = "REJECTED";     // 已拒绝
    public static final String STATUS_RETURNED = "RETURNED";     // 已退回

    // 审核状态常量
    public static final String REVIEW_PENDING = "PENDING";       // 待审核
    public static final String REVIEW_APPROVED = "APPROVED";     // 审核通过
    public static final String REVIEW_REJECTED = "REJECTED";     // 审核拒绝
    public static final String REVIEW_RETURNED = "RETURNED";     // 审核退回

    // 质量检查结果常量
    public static final String QUALITY_EXCELLENT = "EXCELLENT";  // 优秀
    public static final String QUALITY_GOOD = "GOOD";            // 良好
    public static final String QUALITY_FAIR = "FAIR";            // 一般
    public static final String QUALITY_POOR = "POOR";            // 较差
    public static final String QUALITY_FAILED = "FAILED";        // 不合格
}
