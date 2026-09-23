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
 * @author Rui
 * @ClassName QualityEntity
 * @Description
 * @DATE 2023/10/10
 */
@Data
@TableName("TBL_YQNS_QUALITY")
@Schema(name="质量评议")
@Accessors(chain = true)
public class QualityEntity extends BaseReservedProperty {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="PROJECT_ID")
    @Schema(name="项目ID")
    private String projectId;

    @TableField(exist = false)
    private ImplementPlanEntity project;


    @TableField(value="START_TIME")
    @Schema(name="实施时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @TableField(value="ORG_ID")
    @Schema(name="负责科室")
    private String orgId;

	@TableField(exist = false)
	private TblOrganization org;

    @TableField(value="TYPE")
    @Schema(name = "评分表类型 1.审计实施方案制定及执行2.底稿质量 3.报告质量 4.审计管理系统上线 5.奖惩事项")
    private Integer type;

    @TableField(value="SCORE")
    @Schema(name = "方案实施得分")
    private Double score;

    @TableField(exist = false)
    @Schema(name = "考核内容")
    private List<QualityItemEntity> qualityItems;

}
