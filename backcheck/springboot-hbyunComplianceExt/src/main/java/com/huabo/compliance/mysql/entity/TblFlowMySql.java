package com.huabo.compliance.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.*;

/**
 * TblFlow entity. @author MyEclipse Persistence Tools
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_FLOW")
@Schema(name="TblFlowMySql对象")
public class TblFlowMySql implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public final static Integer YES_VSESION = 1;
    public final static Integer NO_VSESION = 0;
    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成

    @TableId("FLOWID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    //@KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order = ORDER.DEFAULT)
    private BigDecimal flowid;
    @Transient
    private BigDecimal riskid;
    @TableField("FLOWNUMBER")
    @Column(name = "FLOWNUMBER")
    private String flownumber;  //编号
    @TableField("FLOWNAME")
    @Column(name = "FLOWNAME")
    private String flowname;    //流程名称
    @TableField("COMPANY")
    @Column(name = "COMPANY")
    private String company;  //流程所属公司
    @TableField("DEPARTINCHARGE")
    @Column(name = "DEPARTINCHARGE")
    private String departincharge;  //责任部门
    @TableField("FLOWRANGE")
    private String flowrange;//使用范围
    @TableField("FLOWSTATUS")
    private String flowstatus;
    @TableField("MEMO")
    private String memo;
    @TableField("VERSION")
    @Column(name = "VERSION")
    private Integer version;
    @TableField("FLOWCHART")
    @Column(name = "FLOWCHART")
    private String flowchart;
    @TableField("DEPARTASSIST")
    private String departassist; //业务参与部门
    @TableField("EDITOR")
    @Column(name = "EDITOR")
    private String editor;  //录入人
    @TableField("UPDATETIME")
    private Date updatetime;
    @TableField("RELATEDRULES")
    private String relatedrules;
    @TableField("AFFECTDEGREE")
    private String affectdegree;
    @TableField("INTERFACE")
    private String interface_;
    @TableField("FATHERFLOWID")
    @Column(name = "FATHERFLOWID")
    private BigDecimal fatherflowid;  //父id
    @TableField("CREATETIME")
    @Column(name = "CREATETIME")
    private String createtime;
    @TableField("LASTMODIFIEDTIME")
    private String lastmodifiedtime;
    @TableField("FLOWBYSYSTEM")
    @Column(name = "FLOWBYSYSTEM")
    private String flowbysystem;//标记  ：  0 无效   1 内部控制 流程图   2风险流程图   区分内控还是风险
    @Transient
    private Set tblOuterrules = new HashSet(0);
    @Transient
    private Set tblRiskevents = new HashSet(0);
    @Transient
    private Set tblRisks = new HashSet(0);
    @Transient
    private Set tblIndicators = new HashSet(0);
    @Transient
    private Set tblInnerrules = new HashSet(0);
    @Transient
    private Set tblFlowdeses = new HashSet(0);
    @Transient
    private Set tblRiskevents_1 = new HashSet(0);
    @Transient
    private Set tblControlmatrixes = new HashSet(0);
    @Transient
    private Set tblflowRiskEvents = new HashSet(0);//流程风险事件
    @TableField("INFLOWDB")
    private Integer inflowdb;//0 流程  1行业
    @TableField("VERSIONTYPE")
    private Integer versionType;
    @Transient
    private String comName;
    @Transient
    private String depName;
    @Transient
    private String deparChargeName;
    @Transient
    private String departissName;
    @Transient
    private String lcLevel;
    @TableField("EDITMODULE")
    private String editModule;
    @TableField("FLOWMAPPINGURL")
    private String flowMappingUrl; //requestMapping对应路径
    @TableField("POSITION")
    private Integer position;  //排序
    @TableField("SETTINGID")
    private String settingid;
    @Transient
    private TblywfromMySql tblywfrom;//业务审批表单
    @TableField("FIRINGSTATUS")
    private Integer firingStatus;//业务流程启动状态 1 启动 2弃用 null 未启动
    // Constructors
    @Transient
    private List<TblFlowUserRigthMySql> flowRightList = new ArrayList<TblFlowUserRigthMySql>(0);
    @TableField("STATUS")
    private Integer status;//审批状态
    //private Set<TblAttachment> tblassetmAtts=new HashSet<>();


}