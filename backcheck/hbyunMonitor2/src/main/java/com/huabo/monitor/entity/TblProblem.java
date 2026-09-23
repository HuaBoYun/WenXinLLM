package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

/**
 * TblProblem entity. @author MyEclipse Persistence Tools
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROBLEM")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblProblem implements java.io.Serializable {
	// Fields
   @TableId(value = "problemid",type = IdType.INPUT)
    @Schema(name="权限主键ID")
	private BigDecimal problemid;
   
   @Column(name="PROBNAME")
   @Schema(name = "问题名")
	private String probname;
   
   @Column(name="PROBDESCRIVE")
   @Schema(name = "描述")
	private String probdescribe;
   
   @Column(name="BUSSINESSBELONGSTO")
   @Schema
	private String bussinessbelongsto;
   
   @Column(name="RISKBELONGSTO")
   @Schema
	private String riskbelongsto;
   
   @Column(name="PROBFROM")
   @Schema
	private String probfrom;
   
   @Column(name="DISCOVERYPERSON")
   @Schema
	private String discoveryperson;
   
   @Column(name="DISCOVERTME")
   @Schema
	private Date discovertime;
   
   @Column(name="OCCURINGTIME")
   @Schema
	private Date occuringtime;
   
   @Column(name="PROBORGS")
   @Schema
	private String proborgs;
   
   @Column(name="NEEDREFORM")
   @Schema
	private String needreform;
   
   @Column(name="MEMO")
   @Schema
	private String memo;
   
   @Column(name="EXPLAINITEM")
   @Schema
	private String explainitem;
   
   @Column(name="PERSONINCHARGE")
   @Schema
	private String personincharge;
   
   @Column(name="PROBNUMBER")
   @Schema
	private String probnumber;
   
   
   @Column(name="PROBLEMBYSYSTEM")
   @Schema
	private String problembysystem;
   
   @TableField(exist = false)
	private Set tblWorksheets = new HashSet(0);
   @TableField(exist = false)
	private Set tblAttachments = new HashSet(0);
   @TableField(exist = false)
	private Set tblReforms = new HashSet(0);
   @TableField(exist = false)
	private Set<TblAssessTarget> tblproblemTargets=new HashSet<TblAssessTarget>();
   @Column(name="INPROBLEMDB")
   @Schema
	private Integer inProblemdb;//0问题库  1 行业
   @Schema
	private Integer  probepartment;
}