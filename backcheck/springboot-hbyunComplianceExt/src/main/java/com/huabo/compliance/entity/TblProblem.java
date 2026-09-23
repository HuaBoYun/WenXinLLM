package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TblProblem entity. @author MyEclipse Persistence Tools
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROBLEM")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class TblProblem implements java.io.Serializable {
	// Fields
	@TableId(type= IdType.INPUT)
	private BigDecimal problemid;
	private String probname;
	private String probdescribe;
	private String bussinessbelongsto;
	private String riskbelongsto;
	private String probfrom;
	private String discoveryperson;
	private Date discovertime;
	private Date occuringtime;
	private String proborgs;
	private String needreform;
	private String memo;
	private String explainitem;
	private String personincharge;
	private String probnumber;
	private String problembysystem;
	private Set tblWorksheets = new HashSet(0);
	private Set tblAttachments = new HashSet(0);
	private Set tblReforms = new HashSet(0);
	private Set<TblAssessTarget> tblproblemTargets=new HashSet<TblAssessTarget>();
	
	private Integer inProblemdb;//0问题库  1 行业
	
	private Integer  probepartment;
}