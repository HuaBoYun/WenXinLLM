package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FLOW")
@Schema(name="TblFlow对象")
public class TblFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "流程ID")
    @TableId("FLOWID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal flowid;

      @Schema(name = "流程编号")
      @TableField("FLOWNUMBER")
    private String flownumber;

      @Schema(name = "流程名称")
      @TableField("FLOWNAME")
    private String flowname;

    @Schema(name = "公司ID")
    @TableField("COMPANY")
    private String company;

      @Schema(name = "部门ID")
      @TableField("DEPARTINCHARGE")
    private String departincharge;

      @Schema(name = "使用范围")
      @TableField("FLOWRANGE")
    private String flowrange;

      @Schema(name = "流程状态")
      @TableField("FLOWSTATUS")
    private String flowstatus;

      @Schema(name = "备注")
      @TableField("MEMO")
    private String memo;

      @Schema(name = "版本")
      @TableField("VERSION")
    private BigDecimal version;

    @TableField("FLOWCHART")
    private String flowchart;

      @Schema(name = "业务参与部门")
      @TableField("DEPARTASSIST")
    private String departassist;

      @Schema(name = "录入人")
      @TableField("EDITOR")
    private String editor;

      @Schema(name = "更新时间")
      @TableField("UPDATETIME")
      @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime updatetime;

    @TableField("RELATEDRULES")
    private String relatedrules;

    @TableField("AFFECTDEGREE")
    private String affectdegree;

    @TableField("INTERFACE")
    private String interfaced;

    @Schema(name = "父流程ID")
    @TableField("FATHERFLOWID")
    private BigDecimal fatherflowid;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
    private String createtime;

      @Schema(name = "最后更改时间")
      @TableField("LASTMODIFIEDTIME")
    private String lastmodifiedtime;

    @TableField("FLOWBYSYSTEM")
    private Integer flowbysystem;

    @TableField("INFLOWDB")
    private BigDecimal inflowdb;

    @TableField("POSITION")
    private BigDecimal position;

    @TableField("VERSIONTYPE")
    private BigDecimal versiontype;

    @TableField("STATUS")
    @Schema(name = "审批状态")
    private BigDecimal status;

    @TableField("SETTINGID")
    @Schema(name = "系统流程Id")
    private String settingid;

    @TableField("FROMID")
    @Schema(name = "表单ID")
    private BigDecimal fromid;

    @TableField("FIRINGSTATUS")
    @Schema(name = "是否启用  1 启动 2弃用 null 未启动")
    private BigDecimal firingstatus;

    @TableField("EDITMODULE")
    @Schema(name = "流程图Id")
    private String editmodule;

    @TableField("FLOWMAPPINGURL")
    @Schema(name = "后台访问地址")
    private String flowmappingurl;

    @Transient
    private TblYwFrom tblywfrom;
}