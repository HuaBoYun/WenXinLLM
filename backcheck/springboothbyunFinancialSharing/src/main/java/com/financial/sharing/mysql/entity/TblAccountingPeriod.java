package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 会计期间实体类
 *
 * @author system
 * @since 2025-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ACCOUNTING_PERIOD")
@ApiModel(value = "TblAccountingPeriod对象", description = "会计期间")
public class TblAccountingPeriod extends Model<TblAccountingPeriod> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "期间ID")
    @TableId(value = "PERIOD_ID", type = IdType.ASSIGN_ID)
    private Long periodId;

    @ApiModelProperty(value = "年度")
    @TableField("YEAR_NO")
    private Integer yearNo;

    @ApiModelProperty(value = "月份")
    @TableField("MONTH_NO")
    private Integer monthNo;

    @ApiModelProperty(value = "期间编码")
    @TableField("PERIOD_CODE")
    private String periodCode;

    @ApiModelProperty(value = "期间名称")
    @TableField("PERIOD_NAME")
    private String periodName;

    @ApiModelProperty(value = "开始日期")
    @TableField("START_DATE")
    private Date startDate;

    @ApiModelProperty(value = "结束日期")
    @TableField("END_DATE")
    private Date endDate;

    @ApiModelProperty(value = "期间状态")
    @TableField("PERIOD_STATUS")
    private String periodStatus;

    @ApiModelProperty(value = "是否当前期间")
    @TableField("IS_CURRENT")
    private Integer isCurrent;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATED_BY")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATED_BY")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATED_TIME", fill = FieldFill.UPDATE)
    private Date updatedTime;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION")
    private Integer version;

    @ApiModelProperty(value = "删除标识")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.periodId;
    }

    /**
     * 期间状态枚举
     */
    public static class PeriodStatus {
        public static final String OPEN = "OPEN";
        public static final String CLOSED = "CLOSED";
        public static final String LOCKED = "LOCKED";
        public static final String CREATED = "CREATED";

        public static final String OPEN_NAME = "开启";
        public static final String CLOSED_NAME = "关闭";
        public static final String LOCKED_NAME = "锁定";
        public static final String CREATED_NAME = "已创建";

        public static String getStatusName(String status) {
            switch (status) {
                case OPEN:
                    return OPEN_NAME;
                case CLOSED:
                    return CLOSED_NAME;
                case LOCKED:
                    return LOCKED_NAME;
                case CREATED:
                    return CREATED_NAME;
                default:
                    return status;
            }
        }
    }
}