package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="现行标准Vo")
public class TblExistingStandardVo {
    @Schema(name = "现行标准ID")
    private BigDecimal id;

    @Schema(name = "文件编号")
    private String ruleCode;

    @Schema(name = "文件名称")
    private String ruleName;

    @Schema(name = "发文文号")
    private String ruleNumber;

    @Schema(name = "发文部门编码")
    private BigDecimal createOrgId;

    @Schema(name = "发文部门名称")
    private String publishOrg;

    @Schema(name = "时效性")
    private String timeLiness;

    @Schema(name = "录入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date takeEffectTime;

    @Schema(name = "录入人id")
    private BigDecimal createStaffId;

    @Schema(name = "录入人")
    private String enteringPerson;

    @Schema(name = "发文日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    @Schema(name = "摘要")
    private String summaryInfo;

    @Schema(name = "逻辑删除")
    private Integer isDelete;

    @Schema(name = "文件id")
    private String attids;

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
