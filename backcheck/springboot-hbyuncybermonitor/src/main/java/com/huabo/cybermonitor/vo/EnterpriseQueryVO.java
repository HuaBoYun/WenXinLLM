package com.huabo.cybermonitor.vo;

import java.time.LocalDateTime;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业信息查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseQueryVO extends BaseVo {

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 企业类型
     */
    private String enterpriseType;

    /**
     * 监管层级
     */
    private String supervisionLevel;

    /**
     * 所属地区
     */
    private String region;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 企业状态
     */
    private String enterpriseStatus;

    /**
     * 上市状态
     */
    private String listingStatus;

    /**
     * 注册资本最小值
     */
    private Double minRegisteredCapital;

    /**
     * 注册资本最大值
     */
    private Double maxRegisteredCapital;

    /**
     * 成立时间开始
     */
    private LocalDateTime establishDateStart;

    /**
     * 成立时间结束
     */
    private LocalDateTime establishDateEnd;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 企业邮箱
     */
    private String enterpriseEmail;

    /**
     * 是否国有控股
     */
    private Boolean isStateControlled;

    /**
     * 是否上市公司
     */
    private Boolean isListed;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 关键词搜索
     */
    private String keyword;
}
