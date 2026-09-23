package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_EXISTING_STANDARD")
@Schema(name="现行标准实体类")
public class TblExistingStandard {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    @Schema(name = "现行标准ID")
    @TableField("ID")
    private BigDecimal id;

    @Schema(name = "文件编号")
    @TableField("RULECODE")
    private String ruleCode;

    @Schema(name = "文件名称")
    @TableField("RULENAME")
    private String ruleName;

    @Schema(name = "发文文号")
    @TableField("RULENUMBER")
    private String ruleNumber;

    @Schema(name = "发文部门编码")
    @TableField("CREATEORGID")
    private BigDecimal createOrgId;

    @Schema(name = "发文部门名称")
    @TableField("PUBLISHORG")
    private String publishOrg;

    @Schema(name = "时效性")
    @TableField("TIMELINESS")
    private String timeLiness;

    @Schema(name = "录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATETIME")
    private Date createTime;

    @Schema(name = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("TAKEEFFECTTIME")
    private Date takeEffectTime;

    @Schema(name = "录入人id")
    @TableField("CREATESTAFFID")
    private BigDecimal createStaffId;

    @Schema(name = "录入人")
    @TableField("ENTERINGPERSON")
    private String enteringPerson;

    @Schema(name = "发文日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("PUBLISHDATE")
    private Date publishDate;

    @Schema(name = "摘要")
    @TableField("SUMMARYINFO")
    private String summaryInfo;

    @Schema(name = "逻辑删除")
    @TableField("IS_DELETE")
    private Integer isDelete;

    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
}
