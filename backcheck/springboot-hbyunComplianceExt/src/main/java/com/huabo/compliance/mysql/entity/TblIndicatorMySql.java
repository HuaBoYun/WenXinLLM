package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INDICATOR")
@Schema(name="TblIndicatorMySql对象")
public class TblIndicatorMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("INDICATORID")
    private BigDecimal indicatorid;

    @TableField("INDICATORCODE")
    private String indicatorcode;

    @TableField("INDICATORNAME")
    private String indicatorname;

    @TableField("FORMULA")
    private String formula;

    @TableField("INDICATORDES")
    private String indicatordes;

    @TableField("MEMO")
    private String memo;

    @TableField("ORGID")
    private Integer orgid;

    @TableField("DEPARTMENTINCHARGE")
    private String departmentincharge;

    @TableField("INDICATORSTATUS")
    private String indicatorstatus;

    @TableField("AUDITINGSTATUS")
    private String auditingstatus;

    @TableField("CREATEDATE")
    private LocalDateTime createdate;

    @TableField("UNITTYPE")
    private String unittype;

    @TableField("FORMULADES")
    private String formulades;

    @TableField("INDCATID")
    private BigDecimal indcatid;

    @TableField("RUNSTATUS")
    private BigDecimal runstatus;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("CONNECTIONSTRINGS")
    private String connectionstrings;

    @TableField("INDICATORDB")
    private Integer indicatordb;

    @TableField("FORLUMACHS")
    private String forlumachs;


    @Transient
    private BigDecimal flowid;
    @Transient
    private BigDecimal riskid;
    @Transient
    private String flownumber;  //编号
    @Transient
    private String flowname;    //流程名称
    @Transient
    private String company;  //流程所属公司
    @Transient
    private String departincharge;  //责任部门
    @Transient
    private String flowrange;//使用范围
    @Transient
    private String flowstatus;
    @Transient
    private double version;
    @Transient
    private String flowchart;
    @Transient
    private String departassist; //业务参与部门
    @Transient
    private String editor;  //录入人
    @Transient
    private Date updatetime;
    @Transient
    private String relatedrules;
    @Transient
    private String affectdegree;
    @Transient
    private String interface_;
    @Transient
    private BigDecimal fatherflowid;  //父id
    @Transient
    private String createtime;
    @Transient
    private String lastmodifiedtime;
    @Transient
    private String flowbysystem;//标记  ：  0 无效   1 内部控制 流程图   2风险流程图   区分内控还是风险
    @Transient
    private Integer inFlowdb;//0 流程  1行业
    @Transient
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
    @Transient
    private String editModule;
    @Transient
    private String flowMappingUrl; //requestMapping对应路径
    @Transient
    private Integer position;  //排序
    @Transient
    private String settingid;
    @Transient
    private TblywfromMySql tblywfrom;//业务审批表单
    @Transient
    private Integer firingStatus;//业务流程启动状态 1 启动 2弃用 null 未启动
    // Constructors
    @Transient
    private List<TblFlowUserRigthMySql> flowRightList = new ArrayList<TblFlowUserRigthMySql>(0);
    @Transient
    private Integer status;//审批状态
    @Transient
    private Set<TblFlowMySql> tblIndicatorFlows = new HashSet();
}
