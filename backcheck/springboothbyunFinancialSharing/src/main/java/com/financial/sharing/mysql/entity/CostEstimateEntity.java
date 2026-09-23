package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本估算实体类 - MySQL版本
 * 
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_COST_ESTIMATE")
public class CostEstimateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 估算ID
     */
    @TableId(value = "ESTIMATE_ID", type = IdType.ASSIGN_ID)
    private Long estimateId;

    /**
     * 估算编号
     */
    @TableField("ESTIMATE_NO")
    private String estimateNo;

    /**
     * 产品ID
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 估算期间
     */
    @TableField("ESTIMATE_PERIOD")
    private String estimatePeriod;

    /**
     * 估算数量
     */
    @TableField("ESTIMATE_QUANTITY")
    private BigDecimal estimateQuantity;

    /**
     * 估算材料成本
     */
    @TableField("ESTIMATED_MATERIAL")
    private BigDecimal estimatedMaterial;

    /**
     * 估算人工成本
     */
    @TableField("ESTIMATED_LABOR")
    private BigDecimal estimatedLabor;

    /**
     * 估算制造费用
     */
    @TableField("ESTIMATED_OVERHEAD")
    private BigDecimal estimatedOverhead;

    /**
     * 总估算成本
     */
    @TableField("TOTAL_ESTIMATED_COST")
    private BigDecimal totalEstimatedCost;

    /**
     * 单位估算成本
     */
    @TableField("UNIT_ESTIMATED_COST")
    private BigDecimal unitEstimatedCost;

    /**
     * 估算状态(1草稿2已确认)
     */
    @TableField("ESTIMATE_STATUS")
    private Integer estimateStatus;

    /**
     * 账簿ID
     */
    @TableField("BOOK_ID")
    private Long bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private Long tenantId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 删除标识(0否1是)
     */
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("CREATOR")
    private Long creator;

    /**
     * 更新人
     */
    @TableField("UPDATER")
    private Long updater;

    // 扩展字段 - 不映射到数据库
    
    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 产品编码
     */
    @TableField(exist = false)
    private String productCode;

    /**
     * 估算状态名称
     */
    @TableField(exist = false)
    private String estimateStatusName;

    /**
     * 创建人姓名
     */
    @TableField(exist = false)
    private String creatorName;

    /**
     * 更新人姓名
     */
    @TableField(exist = false)
    private String updaterName;

    /**
     * 账簿名称
     */
    @TableField(exist = false)
    private String bookName;

    /**
     * 成本差异
     */
    @TableField(exist = false)
    private BigDecimal costVariance;

    /**
     * 差异率
     */
    @TableField(exist = false)
    private BigDecimal varianceRate;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String remark;

    /**
     * 估算方法
     */
    @TableField(exist = false)
    private String estimateMethod;

    /**
     * 估算模型ID
     */
    @TableField(exist = false)
    private Long modelId;

    /**
     * 估算模型名称
     */
    @TableField(exist = false)
    private String modelName;

    /**
     * 估算精度
     */
    @TableField(exist = false)
    private BigDecimal accuracy;

    /**
     * 置信度
     */
    @TableField(exist = false)
    private BigDecimal confidence;

    /**
     * 风险等级
     */
    @TableField(exist = false)
    private String riskLevel;

    /**
     * 审批状态
     */
    @TableField(exist = false)
    private Integer approvalStatus;

    /**
     * 审批状态名称
     */
    @TableField(exist = false)
    private String approvalStatusName;

    /**
     * 审批人
     */
    @TableField(exist = false)
    private Long approver;

    /**
     * 审批人姓名
     */
    @TableField(exist = false)
    private String approverName;

    /**
     * 审批时间
     */
    @TableField(exist = false)
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField(exist = false)
    private String approvalComment;

    /**
     * 是否基准版本
     */
    @TableField(exist = false)
    private Boolean isBaseline;

    /**
     * 基准版本ID
     */
    @TableField(exist = false)
    private Long baselineId;

    /**
     * 父版本ID
     */
    @TableField(exist = false)
    private Long parentId;

    /**
     * 估算场景
     */
    @TableField(exist = false)
    private String scenario;

    /**
     * 估算参数JSON
     */
    @TableField(exist = false)
    private String parametersJson;

    /**
     * 计算公式
     */
    @TableField(exist = false)
    private String formula;

    /**
     * 数据来源
     */
    @TableField(exist = false)
    private String dataSource;

    /**
     * 有效期开始
     */
    @TableField(exist = false)
    private LocalDateTime validFrom;

    /**
     * 有效期结束
     */
    @TableField(exist = false)
    private LocalDateTime validTo;

    /**
     * 币种
     */
    @TableField(exist = false)
    private String currency;

    /**
     * 汇率
     */
    @TableField(exist = false)
    private BigDecimal exchangeRate;

    /**
     * 本币金额
     */
    @TableField(exist = false)
    private BigDecimal localAmount;

    /**
     * 成本中心ID
     */
    @TableField(exist = false)
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    @TableField(exist = false)
    private String costCenterName;

    /**
     * 项目ID
     */
    @TableField(exist = false)
    private Long projectId;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 部门ID
     */
    @TableField(exist = false)
    private Long departmentId;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String departmentName;

    /**
     * 业务类型
     */
    @TableField(exist = false)
    private String businessType;

    /**
     * 优先级
     */
    @TableField(exist = false)
    private Integer priority;

    /**
     * 标签
     */
    @TableField(exist = false)
    private String tags;

    /**
     * 附件路径
     */
    @TableField(exist = false)
    private String attachmentPath;

    /**
     * 扩展属性1
     */
    @TableField(exist = false)
    private String attribute1;

    /**
     * 扩展属性2
     */
    @TableField(exist = false)
    private String attribute2;

    /**
     * 扩展属性3
     */
    @TableField(exist = false)
    private String attribute3;

    /**
     * 扩展属性4
     */
    @TableField(exist = false)
    private String attribute4;

    /**
     * 扩展属性5
     */
    @TableField(exist = false)
    private String attribute5;
}
