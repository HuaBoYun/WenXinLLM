package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算科目映射实体
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Data
@TableName("TBL_BUDGET_ACCOUNT_MAPPING")
public class BudgetAccountMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 映射ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String mappingId;

    /**
     * 预算科目ID
     */
    private String accountId;

    /**
     * 映射系统 (ERP/CRM/OA)
     */
    private String mappingSystem;

    /**
     * 映射编码
     */
    private String mappingCode;

    /**
     * 映射名称
     */
    private String mappingName;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    private Integer delFlag;
}
