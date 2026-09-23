package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 债权管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DebtManagementQueryParam implements Serializable {

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
     * 债权编号
     */
    private String debtNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 债权类型(1:应收账款,2:预付账款,3:其他应收款,4:长期应收款,5:坏账准备)
     */
    private Integer debtType;

    /**
     * 债权类型列表
     */
    private List<Integer> debtTypeList;

    /**
     * 债务人名称
     */
    private String debtorName;

    /**
     * 债务人类型(1:个人,2:企业,3:政府机构,4:其他)
     */
    private Integer debtorType;

    /**
     * 债务人类型列表
     */
    private List<Integer> debtorTypeList;

    /**
     * 债权金额最小值
     */
    private BigDecimal debtAmountMin;

    /**
     * 债权金额最大值
     */
    private BigDecimal debtAmountMax;

    /**
     * 已收回金额最小值
     */
    private BigDecimal collectedAmountMin;

    /**
     * 已收回金额最大值
     */
    private BigDecimal collectedAmountMax;

    /**
     * 剩余金额最小值
     */
    private BigDecimal remainingAmountMin;

    /**
     * 剩余金额最大值
     */
    private BigDecimal remainingAmountMax;

    /**
     * 坏账金额最小值
     */
    private BigDecimal badDebtAmountMin;

    /**
     * 坏账金额最大值
     */
    private BigDecimal badDebtAmountMax;

    /**
     * 债权形成日期开始
     */
    private Date debtDateStart;

    /**
     * 债权形成日期结束
     */
    private Date debtDateEnd;

    /**
     * 到期日期开始
     */
    private Date dueDateStart;

    /**
     * 到期日期结束
     */
    private Date dueDateEnd;

    /**
     * 逾期天数最小值
     */
    private Integer overdueDaysMin;

    /**
     * 逾期天数最大值
     */
    private Integer overdueDaysMax;

    /**
     * 债权状态(1:正常,2:逾期,3:呆账,4:坏账,5:已收回,6:部分收回,7:已核销)
     */
    private Integer debtStatus;

    /**
     * 债权状态列表
     */
    private List<Integer> debtStatusList;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    private Integer riskLevel;

    /**
     * 风险等级列表
     */
    private List<Integer> riskLevelList;

    /**
     * 催收状态(1:未催收,2:催收中,3:催收成功,4:催收失败,5:法律程序)
     */
    private Integer collectionStatus;

    /**
     * 催收状态列表
     */
    private List<Integer> collectionStatusList;

    /**
     * 催收次数最小值
     */
    private Integer collectionCountMin;

    /**
     * 催收次数最大值
     */
    private Integer collectionCountMax;

    /**
     * 最后催收日期开始
     */
    private Date lastCollectionDateStart;

    /**
     * 最后催收日期结束
     */
    private Date lastCollectionDateEnd;

    /**
     * 法律状态(1:正常,2:仲裁中,3:诉讼中,4:执行中)
     */
    private Integer legalStatus;

    /**
     * 法律状态列表
     */
    private List<Integer> legalStatusList;

    /**
     * 律师信息
     */
    private String lawyerInfo;

    /**
     * 法院案件号
     */
    private String courtCaseNo;

    /**
     * 诉讼状态
     */
    private Integer litigationStatus;

    /**
     * 诉讼状态列表
     */
    private List<Integer> litigationStatusList;

    /**
     * 核销原因
     */
    private String writeOffReason;

    /**
     * 核销日期开始
     */
    private Date writeOffDateStart;

    /**
     * 核销日期结束
     */
    private Date writeOffDateEnd;

    /**
     * 核销审批人ID
     */
    private Long writeOffApproverId;

    /**
     * 负责人ID
     */
    private Long responsiblePersonId;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间结束
     */
    private Date updateTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向(ASC/DESC)
     */
    private String orderDirection;

    /**
     * 备注
     */
    private String remarks;
}
