package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投标文件表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bid_document")
public class BidDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 招投标项目ID
     */
    @TableField("bidding_project_id")
    private Long biddingProjectId;

    /**
     * 文件编号
     */
    @TableField("document_no")
    private String documentNo;

    /**
     * 文件名称
     */
    @TableField("document_name")
    private String documentName;

    /**
     * 文件类型(1:技术标,2:商务标,3:资格标,4:其他)
     */
    @TableField("document_type")
    private Integer documentType;

    /**
     * 投标报价
     */
    @TableField("bid_price")
    private BigDecimal bidPrice;

    /**
     * 工期（天）
     */
    @TableField("construction_period")
    private Integer constructionPeriod;

    /**
     * 质量目标
     */
    @TableField("quality_target")
    private String qualityTarget;

    /**
     * 安全目标
     */
    @TableField("safety_target")
    private String safetyTarget;

    /**
     * 技术方案
     */
    @TableField("technical_solution")
    private String technicalSolution;

    /**
     * 商务条款
     */
    @TableField("commercial_terms")
    private String commercialTerms;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 文档状态(1:草稿,2:待审核,3:已审核,4:已提交,5:已中标,6:未中标)
     */
    @TableField("document_status")
    private Integer documentStatus;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private Date submitTime;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @TableField("review_time")
    private Date reviewTime;

    /**
     * 审核意见
     */
    @TableField("review_comments")
    private String reviewComments;

    /**
     * 中标结果(1:中标,2:未中标,3:待定)
     */
    @TableField("bid_result")
    private Integer bidResult;

    /**
     * 中标通知时间
     */
    @TableField("award_notice_time")
    private Date awardNoticeTime;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 获取文件类型名称
     */
    public String getDocumentTypeName() {
        if (documentType == null) {
            return "";
        }
        switch (documentType) {
            case 1:
                return "技术标";
            case 2:
                return "商务标";
            case 3:
                return "资格标";
            case 4:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取文档状态名称
     */
    public String getDocumentStatusName() {
        if (documentStatus == null) {
            return "";
        }
        switch (documentStatus) {
            case 1:
                return "草稿";
            case 2:
                return "待审核";
            case 3:
                return "已审核";
            case 4:
                return "已提交";
            case 5:
                return "已中标";
            case 6:
                return "未中标";
            default:
                return "未知";
        }
    }

    /**
     * 获取中标结果名称
     */
    public String getBidResultName() {
        if (bidResult == null) {
            return "";
        }
        switch (bidResult) {
            case 1:
                return "中标";
            case 2:
                return "未中标";
            case 3:
                return "待定";
            default:
                return "未知";
        }
    }

    /**
     * 获取文档状态颜色
     */
    public String getDocumentStatusColor() {
        if (documentStatus == null) {
            return "#909399";
        }
        switch (documentStatus) {
            case 1:
                return "#909399"; // 灰色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#409EFF"; // 蓝色
            case 4:
                return "#67C23A"; // 绿色
            case 5:
                return "#67C23A"; // 绿色
            case 6:
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }

    /**
     * 获取中标结果颜色
     */
    public String getBidResultColor() {
        if (bidResult == null) {
            return "#909399";
        }
        switch (bidResult) {
            case 1:
                return "#67C23A"; // 绿色
            case 2:
                return "#F56C6C"; // 红色
            case 3:
                return "#E6A23C"; // 橙色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否可以编辑
     */
    public boolean canEdit() {
        return documentStatus != null && (documentStatus == 1 || documentStatus == 3);
    }

    /**
     * 判断是否可以提交
     */
    public boolean canSubmit() {
        return documentStatus != null && documentStatus == 3;
    }

    /**
     * 判断是否已提交
     */
    public boolean isSubmitted() {
        return documentStatus != null && documentStatus >= 4;
    }

    /**
     * 判断是否中标
     */
    public boolean isWinning() {
        return bidResult != null && bidResult == 1;
    }

    /**
     * 获取投标报价显示文本
     */
    public String getBidPriceText() {
        if (bidPrice == null) {
            return "未报价";
        }
        if (bidPrice.compareTo(new BigDecimal("10000")) >= 0) {
            return bidPrice.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return bidPrice.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取工期显示文本
     */
    public String getConstructionPeriodText() {
        if (constructionPeriod == null) {
            return "未设定";
        }
        return constructionPeriod + "天";
    }

    /**
     * 获取文件大小显示文本
     */
    public String getFileSizeText() {
        if (fileSize == null) {
            return "未知";
        }
        if (fileSize < 1024) {
            return fileSize + "B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2fKB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", fileSize / (1024.0 * 1024.0));
        } else {
            return String.format("%.2fGB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 获取文件扩展名
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 判断是否为Word文档
     */
    public boolean isWordDocument() {
        String extension = getFileExtension();
        return "doc".equals(extension) || "docx".equals(extension);
    }

    /**
     * 判断是否为PDF文档
     */
    public boolean isPdfDocument() {
        return "pdf".equals(getFileExtension());
    }

    /**
     * 判断是否为压缩文件
     */
    public boolean isArchiveFile() {
        String extension = getFileExtension();
        return "zip".equals(extension) || "rar".equals(extension) || "7z".equals(extension);
    }

    /**
     * 判断是否为大文件（>=50MB）
     */
    public boolean isLargeFile() {
        return fileSize != null && fileSize >= 50 * 1024 * 1024;
    }
}
