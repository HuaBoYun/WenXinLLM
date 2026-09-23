package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TblFlow entity. @author MyEclipse Persistence Tools
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOW")
@Schema(name="TblFlow对象", description="")
public class TblFlow implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public final static Integer YES_VSESION=1;
	public final static Integer NO_VSESION=0;
	public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成

	@TableId(value="FLOWID",type = IdType.INPUT)
	@Schema(name="主键")
	private BigDecimal flowid;
	@Transient
	@Schema(name="风险主键")
	private BigDecimal riskid;
	@TableField("FLOWNUMBER")
	@Schema(name="业务流程编号")
	private String flownumber;  //编号
	@TableField("FLOWNAME")
	@Schema(name="流程名称")
	private String flowname;	//流程名称
	@TableField("COMPANY")
	@Schema(name="路程所属公司")
	private String company;  //流程所属公司
	@TableField("DEPARTINCHARGE")
	@Schema(name="责任部门")
	private String departincharge;  //责任部门
	@TableField("FLOWRANGE")
	@Schema(name="使用范围")
	private String flowrange;//使用范围
	@TableField("FLOWSTATUS")
	@Schema(name="流程状态")
	private String flowstatus;
	@TableField("MEMO")
	@Schema(name="流程描述")
	private String memo;
	@TableField("VERSION")
	@Schema(name="流程版本")
	private Integer version;
	@TableField("FLOWCHART")
	@Schema(name="流程图路径")
	private String flowchart;
	@TableField("DEPARTASSIST")
	@Schema(name="业务参与不么")
	private String departassist; //业务参与部门
	@TableField("EDITOR")
	@Schema(name="录入人")
	private String editor;  //录入人
	@TableField("UPDATETIME")
	@Schema(name="修改时间")
	private Date updatetime;
	@TableField("RELATEDRULES")
	private String relatedrules;
	@TableField("AFFECTDEGREE")
	private String affectdegree;
	@TableField("INTERFACE")
	private String interface_;
	@TableField("FATHERFLOWID")
	private BigDecimal fatherflowid;  //父id
	@TableField("CREATETIME")
	private String createtime;
	@TableField("LASTMODIFIEDTIME")
	private String lastmodifiedtime;
	@TableField("FLOWBYSYSTEM")
	private String  flowbysystem;//标记  ：  0 无效   1 内部控制 流程图   2风险流程图   区分内控还是风险
	@TableField(exist = false)
	private Set tblOuterrules = new HashSet(0);
	@TableField(exist = false)
	private Set tblRiskevents = new HashSet(0);
	@TableField(exist = false)
	private Set tblRisks = new HashSet(0);
	@TableField(exist = false)
	private Set tblIndicators = new HashSet(0);
	@TableField(exist = false)
	private Set tblInnerrules = new HashSet(0);
	@TableField(exist = false)
	private Set tblFlowdeses = new HashSet(0);
	@TableField(exist = false)
	private Set tblRiskevents_1 = new HashSet(0);
	@TableField(exist = false)
	private Set tblControlmatrixes = new HashSet(0);
	@TableField(exist = false)
	private Set tblflowRiskEvents = new HashSet(0);//流程风险事件
	@TableField("INFLOWDB")
	private Integer inflowdb;//0 流程  1行业
	@TableField("VERSIONTYPE")
	private Integer versionType;
	@TableField(exist = false)
	private String comName;
	@TableField(exist = false)
	private String depName;
	@TableField(exist = false)
	private String deparChargeName;
	@TableField(exist = false)
	private String departissName;
	@TableField(exist = false)
	private String lcLevel;
	@TableField("EDITMODULE")
	private String editModule;
	@TableField("FLOWMAPPINGURL")
	private String flowMappingUrl; //requestMapping对应路径 
	@TableField("POSITION")
	private Integer position;  //排序
	@TableField("SETTINGID")
	private String settingid;
	@TableField(exist = false)
	private Tblywfrom tblywfrom;//业务审批表单
	@TableField("FIRINGSTATUS")
	private Integer firingStatus;//业务流程启动状态 1 启动 2弃用 null 未启动
	// Constructors
	@TableField(exist = false)
	private List<TblFlowUserRigth> flowRightList = new  ArrayList<TblFlowUserRigth>(0);
	@TableField("STATUS")
	 private Integer status;//审批状态
	  //private Set<TblAttachment> tblassetmAtts=new HashSet<>();





}