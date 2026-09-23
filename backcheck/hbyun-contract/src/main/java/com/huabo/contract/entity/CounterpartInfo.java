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
 * 相对方信息表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("counterpart_info")
public class CounterpartInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 公司代码
     */
    @TableField("company_code")
    private String companyCode;

    /**
     * 法定代表人
     */
    @TableField("legal_representative")
    private String legalRepresentative;

    /**
     * 注册资本
     */
    @TableField("registered_capital")
    private BigDecimal registeredCapital;

    /**
     * 经营范围
     */
    @TableField("business_scope")
    private String businessScope;

    /**
     * 信用等级
     */
    @TableField("credit_rating")
    private String creditRating;

    /**
     * 资质等级
     */
    @TableField("qualification_level")
    private String qualificationLevel;

    /**
     * 联系人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 合作历史
     */
    @TableField("cooperation_history")
    private String cooperationHistory;

    /**
     * 黑名单标识(0:否,1:是)
     */
    @TableField("blacklist_flag")
    private Integer blacklistFlag;

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
     * 获取黑名单状态名称
     */
    public String getBlacklistFlagName() {
        if (blacklistFlag == null) {
            return "否";
        }
        return blacklistFlag == 1 ? "是" : "否";
    }

    /**
     * 判断是否为黑名单
     */
    public boolean isBlacklisted() {
        return blacklistFlag != null && blacklistFlag == 1;
    }

    /**
     * 获取信用等级颜色
     */
    public String getCreditRatingColor() {
        if (creditRating == null) {
            return "#909399";
        }
        switch (creditRating.toUpperCase()) {
            case "AAA":
                return "#67C23A"; // 绿色
            case "AA":
            case "A":
                return "#409EFF"; // 蓝色
            case "BBB":
            case "BB":
            case "B":
                return "#E6A23C"; // 橙色
            case "CCC":
            case "CC":
            case "C":
            case "D":
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }

    /**
     * 获取信用等级描述
     */
    public String getCreditRatingDescription() {
        if (creditRating == null) {
            return "未评级";
        }
        switch (creditRating.toUpperCase()) {
            case "AAA":
                return "信用极好";
            case "AA":
                return "信用优良";
            case "A":
                return "信用良好";
            case "BBB":
                return "信用一般";
            case "BB":
            case "B":
                return "信用较差";
            case "CCC":
            case "CC":
            case "C":
                return "信用很差";
            case "D":
                return "违约";
            default:
                return "未知等级";
        }
    }

    /**
     * 判断是否为高信用等级
     */
    public boolean isHighCreditRating() {
        if (creditRating == null) {
            return false;
        }
        String rating = creditRating.toUpperCase();
        return "AAA".equals(rating) || "AA".equals(rating) || "A".equals(rating);
    }

    /**
     * 判断是否为低信用等级
     */
    public boolean isLowCreditRating() {
        if (creditRating == null) {
            return false;
        }
        String rating = creditRating.toUpperCase();
        return "CCC".equals(rating) || "CC".equals(rating) || "C".equals(rating) || "D".equals(rating);
    }

    /**
     * 获取注册资本显示文本
     */
    public String getRegisteredCapitalText() {
        if (registeredCapital == null) {
            return "未知";
        }
        if (registeredCapital.compareTo(new BigDecimal("10000")) >= 0) {
            return registeredCapital.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return registeredCapital.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 判断是否为大型企业（注册资本>=1000万）
     */
    public boolean isLargeEnterprise() {
        return registeredCapital != null && registeredCapital.compareTo(new BigDecimal("10000000")) >= 0;
    }
}
