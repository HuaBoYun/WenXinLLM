package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 科目表
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCCHART")
@Schema(name="BdAccchart对象", description="科目表")
public class BdAccchart implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("PK_ACCCHART")
      private String pkAccchart;

      @Schema(name = "科目表编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "科目表名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "所属科目体系")
      @TableField("PK_ACCSYSTEM")
    private String pkAccsystem;

      @Schema(name = "所属控制规则")
      @TableField("PK_ACCCTRLRULE")
    private String pkAccctrlrule;

      @Schema(name = "版本变更发起表")
      @TableField("ALTERCHART")
    private String alterchart;

      @Schema(name = "原始科目表")
      @TableField("ORIGINALCHART")
    private String originalchart;

      @Schema(name = "生效日期")
      @TableField("BEGINPERIOD")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginperiod;

      @Schema(name = "失效日期")
      @TableField("ENDPERIOD")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endperiod;

      @Schema(name = "科目显示名称格式0=本级名称;1=逐级名称;2=一级名称+本级名称;3=编码+本级名称;4=编码+逐级名称;5=编码+一级名称+本级名称;")
      @TableField("DISPNAMEMODEL")
    private Integer dispnamemodel;

      @Schema(name = "备注")
      @TableField("MEMO")
    private String memo;

      @Schema(name = "创建组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "创建集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "预留规则")
      @TableField("OBLIGATERULE")
    private String obligaterule;

      @Schema(name = "数据来源 0=本级产生; 1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;-2=数据采集")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "内部码")
      @TableField("INNERCODE")
    private String innercode;

      @Schema(name = "-1=控制到末级;0=不控制;1=1;2=2;3=3;4=4;5=5;6=6;7=7;8=8;9=9;10=10;")
      @TableField("CTRLLEVEL")
    private Integer ctrllevel;

      @Schema(name = "允许重复的预留规则")
      @TableField("ALLOWSAMERULE")
    private String allowsamerule;

      @Schema(name = "政策性表关联集团")
      @TableField("POLICYGROUPS")
    private String policygroups;

      @Schema(name = "集团政策性科目表")
      @TableField("ISPLYCHART")
    private String isplychart;

      @Schema(name = "是否代替存在的政策性")
      @TableField("ISREPLACEPLY")
    private String isreplaceply;

      @Schema(name = "核算账簿")
      @TableField("USEDACCBOOK")
    private String usedaccbook;

      @Schema(name = "会计政策科目表")
      @TableField("ACCPOLICYCHART")
    private String accpolicychart;

      @Schema(name = "是否检查集团第一个科目表")
      @TableField("ISCHECKFIRSTCHART")
    private String ischeckfirstchart;

      @Schema(name = "临时版本标识")
      @TableField("TEMPVERSIONFLAG")
    private String tempversionflag;

      @Schema(name = "临时版本主键")
      @TableField("TEMPCHARTID")
    private String tempchartid;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
