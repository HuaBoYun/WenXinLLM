package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name="TblNbsjPlanproject对象")
public class TblNbsjPlanProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableField("PLANPROJECTID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Schema(name = "计划项目ID")
    private BigDecimal planprojectid;

    @TableField("PROJECTNAME")
    @Schema(name = "计划项目名称")
    private String projectname;

    @TableField("TARGETNAME")
    @Schema(name = "工作目标")
    private String targetname;

    @TableField("FINISHTIME")
    @Schema(name = "完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date finishtime;

    @TableField("ORGIDS")
    @Schema(name = "被审计单位（可多选，非必选）")
    private String orgids;

    @TableField("ORGIDNAMES")
    private String orgidnames;

    @TableField("EXTERNALASSIG")
    @Schema(name = "是否外委")
    private Integer externalassig;

    @TableField("PLANID")
    @Schema(name = "关联智能审计计划主键")
    private BigDecimal planid;

    @TableField("BSJTYPE")
    @Schema(name = "人员或部门  bm或yh")
    private String bsjtype;


}
