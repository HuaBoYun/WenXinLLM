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
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_ASSESS")
@Schema(name="TblAssess对象")
public class TblAssess implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name= "评价项目ID")
      @TableId(value="ASSID",type = IdType.INPUT)
    private BigDecimal assid;

      @Schema(name= "评价项目编号")
      @TableField("ASSESSID")
    private String assessid;

      @Schema(name= "开始时间")
      @TableField("STARTDATE")
    private Date startdate;

      @Schema(name= "结束是时间")
      @TableField("ENDDATE")
    private Date enddate;

      @Schema(name= "备注")
      @TableField("MEMO")
    private String memo;

      @Schema(name= "状态(1创建2启动)")
      @TableField("ASSSTATUS")
    private String assstatus;

      @Schema(name= "发起日期")
      @TableField("ASSSTARTDAY")
    private LocalDateTime assstartday;

      @Schema(name= "评价组织")
      @TableField("ASSORGS")
    private String assorgs;

      @Schema(name= "评价项目名称")
      @TableField("ASSESSNAME")
    private String assessname;

      @Schema(name= "评价对象")
      @TableField("ASSSPONSOR")
    private String asssponsor;

      @Schema(name= "归档人")
      @TableField("ARCHIVEPERSON")
    private String archiveperson;

      @Schema(name= "归档时间")
      @TableField("ARCHIVETIME")
    private LocalDateTime archivetime;

      @Schema(name= "初步评价等级")
      @TableField("PRELIMINARYASSLEVEL")
    private String preliminaryasslevel;

      @Schema(name= "初步评价评分")
      @TableField("PRELIMINARYASSSCORE")
    private BigDecimal preliminaryassscore;

      @Schema(name= "校正级别")
      @TableField("ADUSTLEVEL")
    private String adustlevel;

      @Schema(name= "校正原因")
      @TableField("ADJUSTRESON")
    private String adjustreson;

    @TableField("ANALYSISSUMMARY")
    private String analysissummary;

    @TableField("FINALSCORE")
    private BigDecimal finalscore;

    @TableField("ASSTEMID")
    private BigDecimal asstemid;

    @TableField("ASSESSDATE")
    private Date assessdate;

      @Schema(name= "评价负责人")
      @TableField("LEADERID")
    private BigDecimal leaderid;

    @TableField("TBLCOMANY")
    private String tblcomany;

      @Schema(name= "海装新增-缺陷来源项目")
      @TableField("DEFECTSOURCE")
    private String defectsource;

      @Schema(name= "海装新增-缺陷来源文件编号")
      @TableField("SOURCEFILE")
    private String sourcefile;


}
