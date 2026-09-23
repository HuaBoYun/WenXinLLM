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
 * 招投标项目表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bidding_management")
public class BiddingProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 招标编号
     */
    @TableField("bidding_no")
    private String biddingNo;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 业主名称
     */
    @TableField("owner_name")
    private String ownerName;

    /**
     * 项目类型(1:公开招标,2:邀请招标,3:竞争性谈判,4:单一来源)
     */
    @TableField("project_type")
    private Integer projectType;

    /**
     * 招标方式(1:公开招标,2:邀请招标,3:竞争性谈判,4:单一来源)
     */
    @TableField("bidding_method")
    private Integer biddingMethod;

    /**
     * 项目地点
     */
    @TableField("project_location")
    private String projectLocation;

    /**
     * 项目规模
     */
    @TableField("project_scale")
    private String projectScale;

    /**
     * 合同金额
     */
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    /**
     * 投标保证金金额
     */
    @TableField("bid_bond_amount")
    private BigDecimal bidBondAmount;

    /**
     * 履约保证金比例
     */
    @TableField("performance_bond_rate")
    private BigDecimal performanceBondRate;

    /**
     * 中标金额
     */
    @TableField("winning_amount")
    private BigDecimal winningAmount;

    /**
     * 合同签订日期
     */
    @TableField("contract_signing_date")
    private Date contractSigningDate;

    /**
     * 项目经理ID
     */
    @TableField("project_manager_id")
    private Long managerId;

    /**
     * 技术要求
     */
    @TableField("technical_requirements")
    private String technicalRequirements;

    /**
     * 商务要求
     */
    @TableField("commercial_requirements")
    private String commercialRequirements;

    /**
     * 资格要求
     */
    @TableField("qualification_requirements")
    private String qualificationRequirements;

    /**
     * 开标时间
     */
    @TableField("bid_opening_date")
    private Date bidOpeningDate;

    /**
     * 投标截止时间
     */
    @TableField("bid_submission_deadline")
    private Date bidDeadline;

    /**
     * 项目状态(1:发布,2:投标中,3:开标,4:评标,5:中标公示,6:完成,7:流标)
     */
    @TableField("project_status")
    private Integer projectStatus;

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
     * 获取项目类型名称
     */
    public String getProjectTypeName() {
        if (projectType == null) {
            return "";
        }
        switch (projectType) {
            case 1:
                return "公开招标";
            case 2:
                return "邀请招标";
            case 3:
                return "竞争性谈判";
            case 4:
                return "单一来源";
            default:
                return "未知";
        }
    }

    /**
     * 获取项目状态名称
     */
    public String getProjectStatusName() {
        if (projectStatus == null) {
            return "";
        }
        switch (projectStatus) {
            case 1:
                return "发布";
            case 2:
                return "投标中";
            case 3:
                return "开标";
            case 4:
                return "评标";
            case 5:
                return "中标公示";
            case 6:
                return "完成";
            case 7:
                return "流标";
            default:
                return "未知";
        }
    }



    /**
     * 获取项目状态颜色
     */
    public String getProjectStatusColor() {
        if (projectStatus == null) {
            return "#909399";
        }
        switch (projectStatus) {
            case 1:
            case 2:
                return "#409EFF"; // 蓝色
            case 3:
            case 4:
                return "#E6A23C"; // 橙色
            case 5:
                return "#67C23A"; // 绿色
            case 6:
                return "#67C23A"; // 绿色
            case 7:
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }



    /**
     * 判断是否可以投标
     */
    public boolean canBid() {
        if (projectStatus == null || bidDeadline == null) {
            return false;
        }
        return projectStatus == 2 && new Date().before(bidDeadline);
    }

    /**
     * 判断是否已过期
     */
    public boolean isExpired() {
        if (bidDeadline == null) {
            return false;
        }
        return new Date().after(bidDeadline);
    }

    /**
     * 判断是否为重点项目（合同金额>=1000万）
     */
    public boolean isKeyProject() {
        return contractAmount != null && contractAmount.compareTo(new BigDecimal("10000000")) >= 0;
    }

    /**
     * 获取合同金额显示文本
     */
    public String getContractAmountText() {
        if (contractAmount == null) {
            return "未知";
        }
        if (contractAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return contractAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return contractAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取投标保证金金额显示文本
     */
    public String getBidBondAmountText() {
        if (bidBondAmount == null) {
            return "未知";
        }
        if (bidBondAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return bidBondAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return bidBondAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 计算距离投标截止时间的天数
     */
    public long getDaysUntilDeadline() {
        if (bidDeadline == null) {
            return -1;
        }
        long diff = bidDeadline.getTime() - new Date().getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * 判断是否紧急（距离截止时间<=3天）
     */
    public boolean isUrgent() {
        long days = getDaysUntilDeadline();
        return days >= 0 && days <= 3;
    }
}
