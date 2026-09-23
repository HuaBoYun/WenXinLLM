package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @TableId(value = "PLANPROJECTID", type= IdType.INPUT)
    @TableField("PLANPROJECTID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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

  //----新增字段 审计计划管理----
    @TableField(value = "PROJECTORG")
    @Column(name = "PROJECTORG")
    @Schema(name = "项目单位名称ID")
    private String projectorg;

    @TableField(value = "PROJECTTYPE")
    @Column(name = "PROJECTTYPE")
    @Schema(name = "项目类型")
    private String projectType;

    @TableField(value = "COSTS")
    @Column(name = "COSTS")
    @Schema(name = "批复总投资（经费）")
    private BigDecimal costs;

    @TableField(value = "APPPROYEARSTART")
    @Column(name = "APPPROYEARSTART")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "批复的项目起止年限—开头")
    private Date appproyearstart;

    @TableField(value = "APPPROYEAREND")
    @Column(name = "APPPROYEAREND")
    @Schema(name = "批复的项目起止年限—结尾")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date appproyearend;

//    @TableField(value = "ACTPROYEARSTART")
//    @Column(name = "ACTPROYEARSTART")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
//    @Schema(name = "实际的起止年限—开头")
//    private Date actproyearstart;
//
//    @TableField(value = "ACTPROYEAREND")
//    @Column(name = "ACTPROYEAREND")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
//    @Schema(name = "实际的起止年限—结尾")
//    private Date actproyearend;

    @TableField(value = "PROJECTMGDEPTID")
    @Column(name = "PROJECTMGDEPTID")
    @Schema(name = "项目主管部门ID")
    private String projectmgdeptid;

    @TableField(value = "PROJECTMGDEPTNAME")
    @Column(name = "PROJECTMGDEPTNAME")
    @Schema(name = "项目主管部门名称")
    private String projectmgdeptname;

    @TableField(value = "STARTDATE")
    @Column(name = "STARTDATE")
    @Schema(name = "计划审计时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @TableField(value = "ENDDATE")
    @Column(name = "ENDDATE")
    @Schema(name = "计划验收时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;

    @TableField(value = "PROJECTORGADDRESS")
    @Column(name = "PROJECTORGADDRESS")
    @Schema(name = "项目单位地址")
    private String projectorgaddress;

    @TableField(value = "PROJECTLINKMAN")
    @Column(name = "PROJECTLINKMAN")
    @Schema(name = "项目联系人")
    private String projectlinkman;

    @TableField(value = "PROJECTLINKTEL")
    @Column(name = "PROJECTLINKTEL")
    @Schema(name = "联系电话")
    private String projectlinktel;

//    @TableField(value = "IMPLEMENTAION")
//    @Column(name = "IMPLEMENTAION")
//    @Schema(name = "审计实施主体")
//    private String implementaion;

    @TableField(value = "AUDITCODE")
    @Column(name = "AUDITCODE")
    @Schema(name = "审计类型ID")
    private String auditCode;

    @TableField(value = "AUDITTYPE")
    @Column(name = "AUDITTYPE")
    @Schema(name = "审计类型")
    private String auditType;

}
