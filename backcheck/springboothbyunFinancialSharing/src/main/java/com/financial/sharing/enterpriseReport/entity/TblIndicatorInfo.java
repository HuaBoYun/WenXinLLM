package com.financial.sharing.enterpriseReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 指标信息实体类
 * 
 * @author system
 * @since 2026-01-30
 */
@Data
@TableName("TBL_INDICATOR_INFO")
public class TblIndicatorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 指标ID
     */
    @TableId(value = "INDICATOR_ID", type = IdType.ASSIGN_UUID)
    private String indicatorId;

    /**
     * 指标编码
     */
    private String indicatorCode;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标类型：CURRENCY(货币)/QUANTITY(数量)/PRICE(价格)/PERCENT(百分比)/TEXT(文本)/ENUM(枚举)/REFERENCE(参照)/DATE(日期)/LONGTEXT(长文本)/ATTACHMENT(附件)
     */
    private String indicatorType;

    /**
     * 汇总属性：SUM(求和)/AVG(平均)/MAX(最大)/MIN(最小)/NONE(不汇总)
     */
    private String aggregateType;

    /**
     * 数据长度
     */
    private Integer dataLength;

    /**
     * 数据精度
     */
    private Integer dataPrecision;

    /**
     * 枚举值（JSON数组）
     */
    private String enumValues;

    /**
     * 参照维度ID
     */
    private String referenceDimension;

    /**
     * 表单链接
     */
    private String formLink;

    /**
     * 填报说明
     */
    private String fillInstruction;

    /**
     * 状态：ACTIVE(启用)/INACTIVE(停用)
     */
    private String status;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}

