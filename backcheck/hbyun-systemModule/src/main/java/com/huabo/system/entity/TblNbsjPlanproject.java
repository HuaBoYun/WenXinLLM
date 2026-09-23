package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_PLANPROJECT")
@Schema(name="TblNbsjPlanproject对象", description="")
public class TblNbsjPlanproject implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="PLANPROJECTID",type = IdType.INPUT)
    @Schema(name="审计项目计划主键")
    private BigDecimal planprojectid;

    @TableField("PROJECTNAME")
    @Schema(name="项目名称")
    private String projectname;

    @TableField("TARGETNAME")
    @Schema(name="目标")
    private String targetname;

    @TableField("FINISHTIME")
    @Schema(name="计划时间")
    private Date finishtime;

    @TableField("ORGIDS")
    @Schema(name="审计对象主键")
    private String orgids;

    @TableField("ORGIDNAMES")
    @Schema(name="审计对象名称")
    private String orgidnames;

    @TableField("EXTERNALASSIG")
    private String externalassig;

    @TableField("PLANID")
    @Schema(name="计划主键")
    private BigDecimal planid;

    @TableField("BSJTYPE")
    private String bsjtype;

    @TableField("STARTTIME")
    @Schema(name="开始时间")
    private LocalDateTime starttime;

    @TableField("PROJECTMONEY")
    @Schema(name="花费")
    private String projectmoney;

    @TableField("AUDITTYPE")
    @Schema(name="审计类型")
    private String audittype;


}
