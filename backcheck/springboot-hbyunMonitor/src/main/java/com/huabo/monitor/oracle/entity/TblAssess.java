package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ASSESS")
@ApiModel(value="TblAssess对象", description="")
public class TblAssess implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty(value = "评价项目ID")
      @TableField("ASSID")
    private BigDecimal assid;

      @ApiModelProperty(value = "评价项目编号")
      @TableField("ASSESSID")
    private String assessid;

      @ApiModelProperty(value = "开始时间")
      @TableField("STARTDATE")
    private LocalDateTime startdate;

      @ApiModelProperty(value = "结束是时间")
      @TableField("ENDDATE")
    private LocalDateTime enddate;

      @ApiModelProperty(value = "备注")
      @TableField("MEMO")
    private String memo;

      @ApiModelProperty(value = "状态(1创建2启动)")
      @TableField("ASSSTATUS")
    private String assstatus;

      @ApiModelProperty(value = "发起日期")
      @TableField("ASSSTARTDAY")
    private LocalDateTime assstartday;

      @ApiModelProperty(value = "评价组织")
      @TableField("ASSORGS")
    private String assorgs;

      @ApiModelProperty(value = "评价项目名称")
      @TableField("ASSESSNAME")
    private String assessname;

      @ApiModelProperty(value = "评价对象")
      @TableField("ASSSPONSOR")
    private String asssponsor;

      @ApiModelProperty(value = "归档人")
      @TableField("ARCHIVEPERSON")
    private String archiveperson;

      @ApiModelProperty(value = "归档时间")
      @TableField("ARCHIVETIME")
    private LocalDateTime archivetime;

      @ApiModelProperty(value = "初步评价等级")
      @TableField("PRELIMINARYASSLEVEL")
    private String preliminaryasslevel;

      @ApiModelProperty(value = "初步评价评分")
      @TableField("PRELIMINARYASSSCORE")
    private BigDecimal preliminaryassscore;

      @ApiModelProperty(value = "校正级别")
      @TableField("ADUSTLEVEL")
    private String adustlevel;

      @ApiModelProperty(value = "校正原因")
      @TableField("ADJUSTRESON")
    private String adjustreson;

    @TableField("ANALYSISSUMMARY")
    private String analysissummary;

    @TableField("FINALSCORE")
    private BigDecimal finalscore;

    @TableField("ASSTEMID")
    private BigDecimal asstemid;

    @TableField("ASSESSDATE")
    private LocalDateTime assessdate;

      @ApiModelProperty(value = "评价负责人")
      @TableField("LEADERID")
    private BigDecimal leaderid;

    @TableField("TBLCOMANY")
    private String tblcomany;

      @ApiModelProperty(value = "海装新增-缺陷来源项目")
      @TableField("DEFECTSOURCE")
    private String defectsource;

      @ApiModelProperty(value = "海装新增-缺陷来源文件编号")
      @TableField("SOURCEFILE")
    private String sourcefile;


}
