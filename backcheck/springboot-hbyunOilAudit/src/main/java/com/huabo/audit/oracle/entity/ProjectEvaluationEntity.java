package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationEntity
 * @Description
 * @DATE 2024/04/13
 */
@Data
@TableName("TBL_PROJECT_EVALUATION")
@Schema(name="项目评分(审计积分评议)")
@Accessors(chain = true)
public class ProjectEvaluationEntity extends BaseReservedProperty {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="PROJECT_ID")
    @Schema(name="项目ID")
    private String projectId;

    @TableField(exist = false)
    private String projectName;


    @TableField(value="START_TIME")
    @Schema(name="实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private String createUser;

    @TableField(value="ORG_ID")
    @Schema(name="创建部门")
    private String orgId;

	@TableField(exist = false)
	private TblOrganization org;

    @TableField(exist = false)
    @Schema(name = "考核内容")
    private List<ProjectEvaluationItemEntity> qualityItems;

}
