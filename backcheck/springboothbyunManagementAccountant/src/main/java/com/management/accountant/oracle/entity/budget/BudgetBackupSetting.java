package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算备份设置实体
 */
@Data
@TableName("TBL_BUDGET_BACKUP_SETTING")
public class BudgetBackupSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "SETTING_ID", type = IdType.ASSIGN_UUID)
    private String settingId;

    @TableField("SETTING_KEY")
    private String settingKey;

    @TableField("SETTING_VALUE")
    private String settingValue;

    @TableField("SETTING_NAME")
    private String settingName;

    @TableField("SETTING_DESC")
    private String settingDesc;

    @TableField("SETTING_GROUP")
    private String settingGroup;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("IS_DELETED")
    private Integer isDeleted;
}
