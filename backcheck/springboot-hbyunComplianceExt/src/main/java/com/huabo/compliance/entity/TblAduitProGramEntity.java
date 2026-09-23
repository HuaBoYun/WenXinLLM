package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.compliance.util.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@TableName("TBL_NBSJ_AUDITPROGRAM")
@Data
@Schema(name="实体类")
public class TblAduitProGramEntity {

	public static final Integer TEMP_NUMBER=0;//审计模板
	public static final Integer ZY_NUMBER=1;//审计指引

	@TableId(value = "programId", type= IdType.AUTO)
    @Schema(name = "主键id")
    private Integer programId;

    @TableField(value = "target")
    @Schema(hidden=true)
    @IgnoreSwaggerParameter
    private TblTargetTypeEntity target;

    @TableField(value = "businessType")
    @Schema(name = "业务单元")
    private String businessType;

    @TableField(value = "status")
    @Schema(name = "状态")
    private Integer status;

	@TableField(value = "suditProcess")
    @Schema(name = "审计程序")
    private String suditProcess;

	@TableField(value = "riskSource")
    @Schema(name = "风险归属")
    private String riskSource;

	@TableField(value = "riskPoint")
    @Schema(name = "风险描述")
    private String riskPoint;

	@TableField(value = "control")
    @Schema(name = "控制措施")
    private String control;

	@TableField(value = "progamcode")
    @Schema
    private String progamcode;

	@TableField(value = "bioData")
    @Schema(name = "所需资料")
    private String bioData;

	@TableField(value = "updateTime")
    @Schema(hidden=true)
    private Date updateTime;

	@TableField(value = "createTime")
    @Schema(hidden=true)
    private Date createTime;

	@TableField(value = "nbsjTemplete")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private TblNbsjTempleteEntity nbsjTemplete;

	@TableField(value = "authorizations")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private Set<TblNbsjAuthorizationEntity> authorizations;

	@TableField(value = "staff")
    @Schema(hidden=true)
	@IgnoreSwaggerParameter
    private TblStaff staff;

	@TableField(value = "TARGETID")
    @Schema
    private Integer targetId;

	@TableField(value = "TEMPID")
    @Schema
    private Integer tempId;

	@TableField(value = "")
    @Schema(hidden=true)
    private String renyuan;

}
