package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电票账户配置实体类
 * 对应表：TC_ETICKET_ACCOUNT（达梦数据库真实表结构）
 */
@Data
@TableName("TC_ETICKET_ACCOUNT")
public class ETicketAccountConfig {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 关联银行账户ID */
    @TableField("BANK_ACCOUNT_ID")
    private String bankAccountId;

    /** 银行代码 */
    @TableField("BANK_CODE")
    private String bankCode;

    /** 银行名称（前端 bankName 映射） */
    @TableField("BANK_NAME")
    private String bankName;

    /** 账号（前端 accountNumber 映射） */
    @TableField("ACCOUNT_NO")
    private String accountNo;

    /** 客户号 */
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /** 电票状态：ACTIVE/INACTIVE/FROZEN/CLOSED（前端 accountStatus 映射） */
    @TableField("ETICKET_STATUS")
    private String eticketStatus;

    /** 业务范围（前端 accountType 映射） */
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    /** 币种代码（前端 currency 映射） */
    @TableField("CURRENCY_CODES")
    private String currencyCodes;

    /** 日限额 */
    @TableField("DAILY_LIMIT")
    private BigDecimal dailyLimit;

    /** 月限额 */
    @TableField("MONTHLY_LIMIT")
    private BigDecimal monthlyLimit;

    /** 接口地址（前端 apiEndpoint 映射） */
    @TableField("INTERFACE_URL")
    private String interfaceUrl;

    /** 接口端口 */
    @TableField("INTERFACE_PORT")
    private Integer interfacePort;

    /** 证书路径 */
    @TableField("CERT_PATH")
    private String certPath;

    /** 证书密码 */
    @TableField("CERT_PASSWORD")
    private String certPassword;

    /** 操作员信息 */
    @TableField("OPERATOR_INFO")
    private String operatorInfo;

    /** 超时设置(ms) */
    @TableField("TIMEOUT_SETTING")
    private Long timeoutSetting;

    /** 重试次数 */
    @TableField("RETRY_COUNT")
    private Integer retryCount;

    /** 最后连接时间 */
    @TableField("LAST_CONNECT_TIME")
    private Date lastConnectTime;

    /** 连接状态 */
    @TableField("CONNECTION_STATUS")
    private String connectionStatus;

    /** 状态：1-启用，0-停用 */
    @TableField("STATUS")
    private String status;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 创建人 */
    @TableField("CREATE_USER")
    private String createUser;

    /** 更新人 */
    @TableField("UPDATE_USER")
    private String updateUser;

    /** 版本号 */
    @TableField("VERSION_NO")
    private Long versionNo;

    // ---- ALTER TABLE 后新增的真实字段 ----

    /** 账户编码（前端 accountCode） */
    @TableField("ACCOUNT_CODE")
    private String accountCode;

    /** 账户名称（前端 accountName） */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /** 电票系统：ECDS/ACDS（前端 eTicketSystem） */
    @JsonProperty("eTicketSystem")
    @TableField("ETICKET_SYSTEM")
    private String eTicketSystem;

    /** 账户类型：BASIC/GENERAL/SPECIAL/TEMPORARY（前端 accountType） */
    @TableField("ACCOUNT_TYPE")
    private String accountType;

    /** 账户状态：ACTIVE/INACTIVE/FROZEN/CLOSED（前端 accountStatus） */
    @TableField("ACCOUNT_STATUS")
    private String accountStatus;

    /** API端点（前端 apiEndpoint） */
    @TableField("API_ENDPOINT")
    private String apiEndpoint;

    /** 余额限制（前端 balanceLimit） */
    @TableField("BALANCE_LIMIT")
    private BigDecimal balanceLimit;

    // ---- 兼容别名（不映射到数据库，供 syncAliasFields 过渡使用）----

    /** 前端传入的 accountNumber，映射到 ACCOUNT_NO */
    @TableField(exist = false)
    private String accountNumber;
}
