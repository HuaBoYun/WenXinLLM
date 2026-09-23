package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权催收记录实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("debt_collection_log")
@Schema(name="DebtCollectionLog对象", description="债权催收记录")
public class DebtCollectionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "债权ID")
    @TableField("debt_id")
    private Long debtId;

    @Schema(name = "催收日期")
    @TableField("collection_date")
    private Date collectionDate;

    @Schema(name = "催收人员ID")
    @TableField("collector_id")
    private Long collectorId;

    @Schema(name = "催收人员姓名")
    @TableField("collector_name")
    private String collectorName;

    @Schema(name = "催收方式(1:电话催收,2:上门催收,3:书面催收,4:法律催收,5:其他方式)")
    @TableField("collection_method")
    private Integer collectionMethod;

    @Schema(name = "催收结果(1:承诺还款,2:部分还款,3:拒绝还款,4:无法联系,5:其他)")
    @TableField("collection_result")
    private Integer collectionResult;

    @Schema(name = "承诺金额")
    @TableField("promised_amount")
    private BigDecimal promisedAmount;

    @Schema(name = "承诺日期")
    @TableField("promised_date")
    private Date promisedDate;

    @Schema(name = "催收内容")
    @TableField("collection_content")
    private String collectionContent;

    @Schema(name = "债务人反馈")
    @TableField("debtor_feedback")
    private String debtorFeedback;

    @Schema(name = "下次跟进计划")
    @TableField("next_follow_up")
    private String nextFollowUp;

    @Schema(name = "备注")
    @TableField("remarks")
    private String remarks;

    @Schema(name = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(name = "创建人ID")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @Schema(name = "更新人ID")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @Schema(name = "删除标记(0:未删除,1:已删除)")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
