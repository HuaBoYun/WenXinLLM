package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 相对方信息查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CounterpartInfoQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 信用等级
     */
    private String creditRating;

    /**
     * 资质等级
     */
    private String qualificationLevel;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 地址
     */
    private String address;

    /**
     * 黑名单标识(0:否,1:是)
     */
    private Integer blacklistFlag;

    /**
     * 注册资本最小值
     */
    private BigDecimal minRegisteredCapital;

    /**
     * 注册资本最大值
     */
    private BigDecimal maxRegisteredCapital;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 更新开始时间
     */
    private Date updateStartTime;

    /**
     * 更新结束时间
     */
    private Date updateEndTime;

    /**
     * 是否只查询黑名单
     */
    private Boolean onlyBlacklist;

    /**
     * 是否只查询高信用等级
     */
    private Boolean onlyHighCredit;

    /**
     * 是否只查询低信用等级
     */
    private Boolean onlyLowCredit;

    /**
     * 是否只查询大型企业
     */
    private Boolean onlyLargeEnterprise;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;

    /**
     * 关键词搜索（公司名称、公司代码、法定代表人、联系人）
     */
    private String keyword;
}
