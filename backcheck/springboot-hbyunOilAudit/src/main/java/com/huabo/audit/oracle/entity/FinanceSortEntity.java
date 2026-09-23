package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author Rui
 * @ClassName FinanceSortEntity
 * @Description
 * @DATE 2023/9/23
 */
@Data
@TableName("TBL_YQNS_FINANCE_SORT")
@Schema(name="财务专项排序表")
@Accessors(chain = true)
public class FinanceSortEntity implements Serializable {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="SUGGEST_DEPT_ID")
    @Schema(name="建议科室")
    private BigDecimal suggestDeptId;

    @TableField(exist = false)
    private TblOrganization suggestDept;

    @TableField(value="SORT")
    @Schema(name="排序")
    private Integer sort;

    @TableField(value="PROJECT_NAME")
    @Schema(name="审计项目名称")
    private String projectName;

    @TableField(value="PROJECT_PURPOSE")
    @Schema(name="立项理由及审计目的")
    private String projectPurpose;

    @TableField(value="CONCERNS_CONTENT")
    @Schema(name="重点关注内容")
    private String concernsContent;

    @TableField(value="UNIT_RANGE")
    @Schema(name="单位范围")
    private String unitRange;

    @TableField(value="TIME_RANGE")
    @Schema(name="时间范围")
    private String timeRange;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "STATUS")
    @Schema(name="状态")
    private Integer status;
}
