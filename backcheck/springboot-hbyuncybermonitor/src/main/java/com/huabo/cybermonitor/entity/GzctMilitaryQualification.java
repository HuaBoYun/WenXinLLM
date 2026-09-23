package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDate;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_MILITARY_QUALIFICATION")
public class GzctMilitaryQualification extends Model<GzctMilitaryQualification> {
    @TableId(value = "QUAL_ID", type = IdType.ASSIGN_UUID) private String qualId;
    @TableField("COMPANY_ID") private String companyId;
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("QUAL_NAME") private String qualName;
    @TableField("QUAL_TYPE") private String qualType;
    @TableField("QUAL_NO") private String qualNo;
    @TableField("ISSUE_AUTHORITY") private String issueAuthority;
    @TableField("ISSUE_DATE") private LocalDate issueDate;
    @TableField("EXPIRE_DATE") private LocalDate expireDate;
    @TableField("QUAL_STATUS") private String qualStatus;
    @TableField("SECRET_LEVEL") private String secretLevel;
    @TableField("IS_EXPIRING") private String isExpiring;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
