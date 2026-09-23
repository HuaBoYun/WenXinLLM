package com.huabo.cybermonitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业基础信息实体类
 * @author system
 * @date 2024-12-20
 */
@Data
@Schema(name="企业基础信息", description="企业基础信息实体")
public class EnterpriseInfo {

    /**
     * 企业状态常量
     */
    public static final String STATUS_NORMAL = "NORMAL";                                 // 正常
    public static final String STATUS_ABNORMAL = "ABNORMAL";                             // 异常
    public static final String STATUS_CANCELLED = "CANCELLED";                           // 已注销
    public static final String STATUS_SUSPENDED = "SUSPENDED";                           // 已暂停

    /**
     * 上市状态常量
     */
    public static final String LISTING_LISTED = "LISTED";                                // 已上市
    public static final String LISTING_UNLISTED = "UNLISTED";                            // 未上市
    public static final String LISTING_DELISTED = "DELISTED";                            // 已退市

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "统一社会信用代码")
    private String creditCode;

    @Schema(name = "企业类型")
    private String enterpriseType;

    @Schema(name = "注册资本（万元）")
    private BigDecimal registeredCapital;

    @Schema(name = "成立日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date establishDate;

    @Schema(name = "法定代表人")
    private String legalRepresentative;

    @Schema(name = "注册地址")
    private String registeredAddress;

    @Schema(name = "经营范围")
    private String businessScope;

    @Schema(name = "行业分类代码")
    private String industryCode;

    @Schema(name = "监管层级")
    private String supervisionLevel;

    @Schema(name = "母公司ID")
    private String parentEnterpriseId;

    @Schema(name = "企业状态")
    private String enterpriseStatus;

    @Schema(name = "联系人")
    private String contactPerson;

    @Schema(name = "联系电话")
    private String contactPhone;

    @Schema(name = "联系邮箱")
    private String contactEmail;

    @Schema(name = "地区代码")
    private String regionCode;

    @Schema(name = "上市状态")
    private String listingStatus;

    @Schema(name = "股票代码")
    private String stockCode;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "更新人")
    private String updateBy;
}
