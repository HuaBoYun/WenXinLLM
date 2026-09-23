package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股权结构实体类
 * @author system
 * @date 2024-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("EQUITY_STRUCTURE")
@Schema(name="股权结构", description="股权结构实体")
public class EquityStructure {

    /**
     * 投资方类型常量
     */
    public static final String INVESTOR_TYPE_ENTERPRISE = "ENTERPRISE";                  // 企业
    public static final String INVESTOR_TYPE_INDIVIDUAL = "INDIVIDUAL";                  // 个人
    public static final String INVESTOR_TYPE_GOVERNMENT = "GOVERNMENT";                  // 政府
    public static final String INVESTOR_TYPE_INSTITUTION = "INSTITUTION";                // 机构
    public static final String INVESTOR_TYPE_FUND = "FUND";                              // 基金
    public static final String INVESTOR_TYPE_OTHER = "OTHER";                            // 其他

    /**
     * 投资方式常量
     */
    public static final String INVESTMENT_METHOD_CASH = "CASH";                          // 现金投资
    public static final String INVESTMENT_METHOD_ASSET = "ASSET";                        // 资产投资
    public static final String INVESTMENT_METHOD_EQUITY = "EQUITY";                      // 股权投资
    public static final String INVESTMENT_METHOD_DEBT = "DEBT";                          // 债权投资
    public static final String INVESTMENT_METHOD_MIXED = "MIXED";                        // 混合投资

    /**
     * 股权性质常量
     */
    public static final String EQUITY_NATURE_STATE_OWNED = "STATE_OWNED";                // 国有股权
    public static final String EQUITY_NATURE_COLLECTIVE = "COLLECTIVE";                  // 集体股权
    public static final String EQUITY_NATURE_PRIVATE = "PRIVATE";                        // 民营股权
    public static final String EQUITY_NATURE_FOREIGN = "FOREIGN";                        // 外资股权
    public static final String EQUITY_NATURE_MIXED = "MIXED";                            // 混合股权

    /**
     * 股权状态常量
     */
    public static final String EQUITY_STATUS_NORMAL = "NORMAL";                          // 正常
    public static final String EQUITY_STATUS_PLEDGED = "PLEDGED";                        // 已质押
    public static final String EQUITY_STATUS_FROZEN = "FROZEN";                          // 冻结
    public static final String EQUITY_STATUS_TRANSFERRED = "TRANSFERRED";                // 已转让
    public static final String EQUITY_STATUS_CANCELLED = "CANCELLED";                    // 已注销

    /**
     * 股权来源常量
     */
    public static final String EQUITY_SOURCE_INITIAL = "INITIAL";                        // 初始投资
    public static final String EQUITY_SOURCE_INCREASE = "INCREASE";                      // 增资扩股
    public static final String EQUITY_SOURCE_TRANSFER = "TRANSFER";                      // 股权转让
    public static final String EQUITY_SOURCE_MERGER = "MERGER";                          // 合并重组
    public static final String EQUITY_SOURCE_SPLIT = "SPLIT";                            // 分立
    public static final String EQUITY_SOURCE_OTHER = "OTHER";                            // 其他

    @TableId(value = "EQUITY_ID", type = IdType.ASSIGN_UUID)
    @Schema(name = "股权ID")
    private String equityId;

    @TableField("ENTERPRISE_ID")
    @Schema(name = "被投资企业ID")
    private String enterpriseId;

    @TableField("INVESTOR_ID")
    @Schema(name = "投资方ID")
    private String investorId;

    @TableField("INVESTOR_TYPE")
    @Schema(name = "投资方类型")
    private String investorType;

    @TableField("INVESTOR_NAME")
    @Schema(name = "投资方名称")
    private String investorName;

    @TableField("SHAREHOLDING_RATIO")
    @Schema(name = "持股比例（%）")
    private BigDecimal shareholdingRatio;

    @TableField("SHAREHOLDING_AMOUNT")
    @Schema(name = "持股金额（万元）")
    private BigDecimal shareholdingAmount;

    @TableField("SHARE_TYPE")
    @Schema(name = "股份类型")
    private String shareType;

    @TableField("VOTING_RATIO")
    @Schema(name = "表决权比例（%）")
    private BigDecimal votingRatio;

    @TableField("CONTROL_TYPE")
    @Schema(name = "控制类型")
    private String controlType;

    @TableField("INVESTMENT_DATE")
    @Schema(name = "投资日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date investmentDate;

    @TableField("EFFECTIVE_DATE")
    @Schema(name = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date effectiveDate;

    @TableField("EXPIRY_DATE")
    @Schema(name = "失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryDate;

    @TableField("STATUS")
    @Schema(name = "状态")
    private String status;

    @TableField("CREATE_TIME")
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @Schema(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
