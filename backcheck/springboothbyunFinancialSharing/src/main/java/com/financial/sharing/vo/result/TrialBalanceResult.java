package com.financial.sharing.vo.result;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 试算平衡结果
 *
 * @author system
 * @since 2024-12-19
 */
@Data
public class TrialBalanceResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 会计期间
     */
    private String accountingPeriod;

    /**
     * 试算平衡时间
     */
    private Date trialTime;

    /**
     * 是否平衡
     */
    private Boolean isBalanced;

    /**
     * 借方合计
     */
    private BigDecimal totalDebit;

    /**
     * 贷方合计
     */
    private BigDecimal totalCredit;

    /**
     * 差额
     */
    private BigDecimal difference;

    /**
     * 平衡率（百分比）
     */
    private BigDecimal balanceRate;

    /**
     * 科目余额列表
     */
    private List<SubjectBalance> subjectBalances;

    /**
     * 错误信息列表
     */
    private List<BalanceError> errors;

    /**
     * 警告信息列表
     */
    private List<BalanceWarning> warnings;

    /**
     * 验证结果
     */
    private ValidationResult validationResult;

    /**
     * 统计信息
     */
    private TrialBalanceStatistics statistics;

    /**
     * 报告文件路径
     */
    private String reportFilePath;

    /**
     * 科目余额内部类
     */
    @Data
    public static class SubjectBalance {
        /**
         * 科目ID
         */
        private Long subjectId;

        /**
         * 科目编码
         */
        private String subjectCode;

        /**
         * 科目名称
         */
        private String subjectName;

        /**
         * 科目类型（1资产2负债3权益4收入5费用）
         */
        private Integer subjectType;

        /**
         * 余额方向（1借方2贷方）
         */
        private Integer balanceDirection;

        /**
         * 期初余额
         */
        private BigDecimal openingBalance;

        /**
         * 借方发生额
         */
        private BigDecimal debitAmount;

        /**
         * 贷方发生额
         */
        private BigDecimal creditAmount;

        /**
         * 期末余额
         */
        private BigDecimal closingBalance;

        /**
         * 累计借方发生额
         */
        private BigDecimal accumulatedDebit;

        /**
         * 累计贷方发生额
         */
        private BigDecimal accumulatedCredit;

        /**
         * 是否正常
         */
        private Boolean isNormal;

        /**
         * 错误信息
         */
        private String errorMessage;
    }

    /**
     * 错误信息内部类
     */
    @Data
    public static class BalanceError {
        /**
         * 错误类型
         */
        private String errorType;

        /**
         * 错误级别
         */
        private String errorLevel; // FATAL, ERROR, WARN

        /**
         * 错误描述
         */
        private String description;

        /**
         * 影响的科目ID
         */
        private Long subjectId;

        /**
         * 影响的科目名称
         */
        private String subjectName;

        /**
         * 错误金额
         */
        private BigDecimal errorAmount;

        /**
         * 建议修正方案
         */
        private String suggestion;
    }

    /**
     * 警告信息内部类
     */
    @Data
    public static class BalanceWarning {
        /**
         * 警告类型
         */
        private String warningType;

        /**
         * 警告描述
         */
        private String description;

        /**
         * 影响的科目ID
         */
        private Long subjectId;

        /**
         * 影响的科目名称
         */
        private String subjectName;

        /**
         * 建议事项
         */
        private String recommendation;
    }

    /**
     * 验证结果内部类
     */
    @Data
    public static class ValidationResult {
        /**
         * 借贷平衡验证
         */
        private Boolean debitCreditBalance;

        /**
         * 科目余额方向验证
         */
        private Boolean balanceDirectionCheck;

        /**
         * 连续性验证
         */
        private Boolean continuityCheck;

        /**
         * 勾稽关系验证
         */
        private Boolean crossReferenceCheck;

        /**
         * 逻辑性验证
         */
        private Boolean logicCheck;

        /**
         * 验证总分
         */
        private Integer totalScore;

        /**
         * 验证等级（A/B/C/D）
         */
        private String validationGrade;
    }

    /**
     * 统计信息内部类
     */
    @Data
    public static class TrialBalanceStatistics {
        /**
         * 总科目数
         */
        private Integer totalSubjects;

        /**
         * 有余额科目数
         */
        private Integer subjectsWithBalance;

        /**
         * 零余额科目数
         */
        private Integer zeroBalanceSubjects;

        /**
         * 异常数目
         */
        private Integer errorCount;

        /**
         * 警告数目
         */
        private Integer warningCount;

        /**
         * 最大余额科目
         */
        private String maxBalanceSubject;

        /**
         * 最大余额金额
         */
        private BigDecimal maxBalanceAmount;

        /**
         * 资产类科目数
         */
        private Integer assetSubjects;

        /**
         * 负债类科目数
         */
        private Integer liabilitySubjects;

        /**
         * 权益类科目数
         */
        private Integer equitySubjects;

        /**
         * 收入类科目数
         */
        private Integer revenueSubjects;

        /**
         * 费用类科目数
         */
        private Integer expenseSubjects;
    }
}