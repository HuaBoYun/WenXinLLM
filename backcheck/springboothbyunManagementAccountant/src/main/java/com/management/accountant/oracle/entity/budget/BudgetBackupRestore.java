package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("TBL_BUDGET_BACKUP_RESTORE")
public class BudgetBackupRestore implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BACKUP_ID", type = IdType.ASSIGN_UUID)
    private String backupId;
    @TableField("BACKUP_CODE")
    private String backupCode;
    @TableField("BACKUP_NAME")
    private String backupName;
    @TableField("BACKUP_TYPE")
    private String backupType;
    @TableField("BACKUP_SCOPE")
    private String backupScope;
    @TableField("BACKUP_PATH")
    private String backupPath;
    @TableField("BACKUP_SIZE")
    private BigDecimal backupSize;
    @TableField("BACKUP_STATUS")
    private String backupStatus;
    @TableField("START_TIME")
    private Date startTime;
    @TableField("END_TIME")
    private Date endTime;
    @TableField("DURATION_SECONDS")
    private Integer durationSeconds;
    @TableField("TABLE_COUNT")
    private Integer tableCount;
    @TableField("RECORD_COUNT")
    private Integer recordCount;
    @TableField("IS_COMPRESSED")
    private Integer isCompressed;
    @TableField("IS_ENCRYPTED")
    private Integer isEncrypted;
    @TableField("RESTORE_STATUS")
    private String restoreStatus;
    @TableField("RESTORE_TIME")
    private Date restoreTime;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATOR_ID")
    private String creatorId;
    @TableField("CREATOR_NAME")
    private String creatorName;
    @TableField("CREATE_TIME")
    private Date createTime;
    @TableField("IS_DELETED")
    private Integer isDeleted;
    @TableField("COMPANY_ID")
    private String companyId;
}

