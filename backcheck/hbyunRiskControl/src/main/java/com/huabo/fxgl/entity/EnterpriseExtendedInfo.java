package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业扩展信息实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_EXTENDED_INFO")
@Schema(name="EnterpriseExtendedInfo", description="企业扩展信息")
public class EnterpriseExtendedInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="扩展信息ID")
    @TableId(value = "EXTENDED_ID", type = IdType.ASSIGN_ID)
    private String extendedId;

    @Schema(name="企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name="注册地址")
    @TableField("REGISTERED_ADDRESS")
    private String registeredAddress;

    @Schema(name="办公地址")
    @TableField("OFFICE_ADDRESS")
    private String officeAddress;

    @Schema(name="联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @Schema(name="传真号码")
    @TableField("CONTACT_FAX")
    private String contactFax;

    @Schema(name="电子邮箱")
    @TableField("EMAIL")
    private String email;

    @Schema(name="官方网站")
    @TableField("WEBSITE")
    private String website;

    @Schema(name="邮政编码")
    @TableField("POSTAL_CODE")
    private String postalCode;

    @Schema(name="营业执照号")
    @TableField("BUSINESS_LICENSE_NO")
    private String businessLicenseNo;

    @Schema(name="税务登记号")
    @TableField("TAX_REGISTRATION_NO")
    private String taxRegistrationNo;

    @Schema(name="组织机构代码")
    @TableField("ORGANIZATION_CODE")
    private String organizationCode;

    @Schema(name="统一社会信用代码")
    @TableField("UNIFIED_SOCIAL_CREDIT_CODE")
    private String unifiedSocialCreditCode;

    @Schema(name="上市状态(LISTED-已上市,UNLISTED-未上市,PREPARING-筹备上市)")
    @TableField("LISTING_STATUS")
    private String listingStatus;

    @Schema(name="上市交易所")
    @TableField("LISTING_EXCHANGE")
    private String listingExchange;

    @Schema(name="股票代码")
    @TableField("STOCK_CODE")
    private String stockCode;

    @Schema(name="创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name="创建人")
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(name="更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name="更新人")
    @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
}
